package com.dearos.clickandrace.ui.screens.appScreens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.authRepository.LoginRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla principal (HomeScreen) que gestiona el estado de la UI y las interacciones
 * con la sesión del usuario.
 *
 * @param loginRepository Repositorio utilizado para manejar las operaciones de autenticación (como logout).
 * @param supabaseClient Cliente de Supabase utilizado para interactuar con los datos del usuario y la sesión.
 */
class HomeViewModel(
    private val loginRepository: LoginRepo,
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    // Estado interno de la UI, almacenado en un flujo mutable
    private val _uiState = MutableStateFlow(HomeUiState())
    // Estado de la UI accesible de forma inmutable desde otras capas
    val uiState: StateFlow<HomeUiState> = _uiState

    // Inicializa la carga de la sesión al crear el ViewModel
    init {
        loadSessionData()
    }

    /**
     * Carga los datos de la sesión actual del usuario.
     *
     * Intenta obtener la sesión actual de Supabase, y si es exitosa, recupera el nombre del usuario,
     * el token de acceso y el token de actualización. En caso de error, se maneja el fallo y
     * se mantiene la sesión anterior o se establece como 'Guest'.
     */
    private fun loadSessionData() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                // Obtener la sesión actual de Supabase
                val session = supabaseClient.auth.currentSessionOrNull()
                val access = session?.accessToken ?: "No Access Token"
                val refresh = session?.refreshToken ?: "No Refresh Token"

                // Recuperar los detalles del usuario para la sesión actual
                val userResponse = supabaseClient.auth.retrieveUserForCurrentSession(updateSession = true)
                val fullNameValue = userResponse.userMetadata?.get("full_name")
                val userName = fullNameValue?.toString() ?: userResponse.email ?: "Guest"

                // Actualizar el estado de la UI con la información del usuario
                _uiState.value = HomeUiState(
                    userName = userName,
                    accessToken = access,
                    refreshToken = refresh,
                    errorMessage = null,
                    isLoading = false
                )
            } catch (e: Exception) {
                // Manejo de errores en caso de fallo al obtener los datos del usuario
                val session = supabaseClient.auth.currentSessionOrNull()
                val access = session?.accessToken ?: "No Access Token"
                val refresh = session?.refreshToken ?: "No Refresh Token"
                _uiState.value = HomeUiState(
                    userName = "Guest",
                    accessToken = access,
                    refreshToken = refresh,
                    errorMessage = "Failed to fetch user details: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    /**
     * Realiza el logout del usuario y ejecuta un callback si es exitoso.
     *
     * @param onLogoutSuccess Callback que se ejecuta cuando el logout es exitoso.
     */
    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = loginRepository.logoutUser()
            if (result.isSuccess) {
                onLogoutSuccess()
            } else {
                // En caso de error al hacer logout, se muestra un mensaje de error
                _uiState.value = _uiState.value.copy(errorMessage = "Logout failed")
            }
        }
    }
}
