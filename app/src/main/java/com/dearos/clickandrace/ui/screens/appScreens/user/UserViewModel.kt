package com.dearos.clickandrace.ui.screens.appScreens.user

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.appRepository.UserRepository
import com.dearos.clickandrace.auth.domain.authRepository.LoginRepo
import com.dearos.clickandrace.data.model.User
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val authRepo: LoginRepo
) : ViewModel() {

    init {
        require(userRepository != null) { "UserRepository no inyectado" }
        require(authRepo != null) { "LoginRepo no inyectado" }
    }

        // Estado de autenticación
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    // Datos del usuario actual
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // Inicializa el estado al cargar el ViewModel
    init {
        viewModelScope.launch {
            // Actualiza el estado de autenticación al iniciar
            checkAuthState()
        }
    }


    private suspend fun checkAuthState() {
        val session = authRepo.supabaseClient.auth.currentSessionOrNull()
        val userId = session?.user?.id
        if (userId != null) {
            val user = userRepository.getUserById(userId).getOrNull()
            _user.emit(user)
            _authState.emit(AuthState.Authenticated)
        } else {
            _authState.emit(AuthState.Unauthenticated)
        }
    }

//    // Verifica si el usuario está autenticado
//    private suspend fun checkAuthState() {
//        val session = authRepo.supabaseClient.auth.currentSessionOrNull()
//        if (session != null) {
//            // Si está autenticado, obtén sus datos
//            val user = userRepository.getUserById(session.user.id).getOrNull()
//            _user.emit(user)
//            _authState.emit(AuthState.Authenticated)
//        } else {
//            _authState.emit(AuthState.Unauthenticated)
//        }
//    }

    // Funciones de autenticación
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepo.loginUser(email, password).onSuccess {
                checkAuthState()
            }.onFailure { error ->
                // Maneja errores (ej: muestra un snackbar)
            }
        }
    }

    fun googleSignIn(activity: Activity) {
        viewModelScope.launch {
            authRepo.googleSignIn(activity).onSuccess {
                checkAuthState()
            }.onFailure { error ->
                // Maneja errores
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepo.logoutUser().onSuccess {
                _user.emit(null)
                _authState.emit(AuthState.Unauthenticated)
            }
        }
    }
}

// Enum para estados de autenticación
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
}