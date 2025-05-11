package com.dearos.clickandrace.ui.screens.main
/*
class MainViewModel {
}

package com.dearos.clickandrace.auth.presentation.main
*/
import SessionManager
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.LogsLogger
import com.dearos.clickandrace.ui.navigation.AuthScreen
import com.dearos.clickandrace.util.LanguageLocaleHelper
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Clase de estado de la UI que contiene la información de carga y la ruta de destino de inicio.
 *
 * @property isLoading Indica si la aplicación está en estado de carga.
 * @property startDestination La ruta a la que debe navegar la aplicación al iniciar.
 */
data class MainUiState(
    val isLoading: Boolean = true,
    val startDestination: String = AuthScreen.Login.route
)

/**
 * ViewModel encargado de gestionar la lógica de inicio de la aplicación, incluyendo la autenticación
 * de usuario y la inicialización del estado de la UI.
 *
 * @param application La instancia de la aplicación, necesaria para acceder a recursos como SharedPreferences.
 * @param supabaseClient Cliente de Supabase para gestionar la autenticación y sesiones.
 */
class MainViewModel(
    application: Application,
    private val supabaseClient: SupabaseClient
) : AndroidViewModel(application) {

    // Estado interno mutable que observa la UI.
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState

    // SharedFlow para emitir mensajes de depuración o de tipo "toast".
    private val _toastMessageFlow = MutableSharedFlow<String>()
    val toastMessageFlow = _toastMessageFlow.asSharedFlow()


//    private val _language = MutableStateFlow(LanguageLocaleHelper.getCurrentLanguage())
//    val language = _language

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Espera a que el sistema de autenticación de Supabase se inicialice.
            awaitSessionInitialization()

            // Recupera SharedPreferences usando el contexto de la aplicación.
            val prefs = getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val pendingReset = prefs.getBoolean("pending_reset_password", false)

            if (pendingReset) {
                // Si existe un flag de reinicio pendiente, cierra sesión al usuario.
                supabaseClient.auth.signOut()  // Fuerza el cierre de sesión si hay un reinicio pendiente
                // Elimina el flag para no afectar futuros lanzamientos de la aplicación.
                prefs.edit().remove("pending_reset_password").apply()
                LogsLogger.d("MainViewModel", "MainViewModel: if (pendingReset) part : $pendingReset")
            } else {
                // Si no hay reinicio pendiente, asegura que cualquier flag obsoleto se elimine.
                prefs.edit().remove("pending_reset_password").apply()
                LogsLogger.d("MainViewModel", "MainViewModel: else {} part: $pendingReset")
            }

            // Verifica si existe una sesión válida.
            val session = supabaseClient.auth.currentSessionOrNull()
            val destination = if (session != null) AuthScreen.HomeScreen.route else AuthScreen.Login.route

            // Actualiza el estado de la UI en el hilo principal.
            withContext(Dispatchers.Main) {
                _uiState.value = MainUiState(isLoading = false, startDestination = destination)
                //TODO
                SessionManager.saveSession(getApplication(), session != null)
            }
        }
        // Comienza a escuchar eventos de autenticación.
        listenToAuthEvents()
    }

    /**
     * Suspende hasta que el sistema de autenticación de Supabase emita un estado que no sea "Initializing".
     */
    private suspend fun awaitSessionInitialization() {
        // Espera que el estado de la sesión deje de estar en inicialización.
        supabaseClient.auth.sessionStatus.first { status ->
            status !is SessionStatus.Initializing
        }
    }


    /**
     * Escucha los eventos de autenticación de Supabase y emite mensajes correspondientes según el estado.
     */
    private fun listenToAuthEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            // Se suscribe a los eventos de estado de la sesión.
            supabaseClient.auth.sessionStatus.collect { status ->
                when (status) {
                    // Si el usuario está autenticado, obtiene el token de acceso y emite un mensaje.
                    is SessionStatus.Authenticated -> {
                        val token = supabaseClient.auth.currentAccessTokenOrNull()
                        LogsLogger.d("Auth", "Authenticated: Access Token: $token")
                        _toastMessageFlow.emit("Authenticated")
                    }
                    // Si el usuario no está autenticado, emite un mensaje correspondiente.
                    is SessionStatus.NotAuthenticated -> {
                        LogsLogger.d("Auth", "User is not authenticated")
                        _toastMessageFlow.emit("User is not authenticated")
                    }
                    // Si la autenticación está en proceso de inicialización, emite un mensaje de estado.
                    SessionStatus.Initializing -> {
                        LogsLogger.d("Auth", "Auth is initializing")
                        _toastMessageFlow.emit("Auth is initializing")
                    }
                    // Si la actualización de la sesión falla, emite un mensaje con el error.
                    is SessionStatus.RefreshFailure -> {
                        LogsLogger.d("Auth", "Session refresh failed: ${status.cause}")
                        _toastMessageFlow.emit("Session refresh failed: ${status.cause}")
                    }
                    // Para otros estados de autenticación, se registra el estado.
                    else -> {
                        LogsLogger.d("Auth", "Other auth state: $status")
                    }
                }
            }
        }
    }


}