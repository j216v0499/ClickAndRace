package com.dearos.clickandrace.ui.screens.authScreens.login


import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.authRepository.LoginRepo
import com.dearos.clickandrace.auth.domain.authRepository.ResetPasswordRepo

//import com.dearos.clickandrace.auth.domain.repository
//import com.dearos.clickandrace.auth.domain.repository.ResetPasswordRepository
import com.dearos.clickandrace.auth.domain.use_case.ValidateEmail
import com.dearos.clickandrace.auth.domain.use_case.ValidatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/*
class LoginViewModel {
}
*/


/**
 * ViewModel responsable de gestionar el estado y la lógica del formulario de login.
 *
 * @property context Contexto de la aplicación.
 * @property validateEmail Caso de uso para validar el email.
 * @property validatePassword Caso de uso para validar la contraseña.
 * @property loginRepository Repositorio para autenticación de usuarios.
 * @property resetPasswordRepository Repositorio para operaciones de recuperación de contraseña.
 *
 * @Suppress("StaticFieldLeak") desactiva la advertencia por posibles fugas de memoria al usar Context en campos estáticos.
 *
 */
@Suppress("StaticFieldLeak")
class LoginViewModel(
    private val context: Context,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val loginRepository: LoginRepo,
    private val resetPasswordRepository: ResetPasswordRepo
) : ViewModel() {

    /** Estado del formulario de login */
    var state by mutableStateOf(LoginFormState())

    /** Canal para emitir eventos de validación */
    private val validationEventChannel = Channel<ValidationEvent>()

    /** Flujo observable de eventos de validación */
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Maneja eventos generados desde la interfaz de usuario.
     *
     * @param event Evento del formulario de login.
     */
    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is LoginFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is LoginFormEvent.Submit -> {
                submitData()
            }
            is LoginFormEvent.GoogleSignIn -> {
                googleSignIn(event.activity)
            }
        }
    }

    /**
     * Valida los campos del formulario y realiza el login del usuario.
     */
    private fun submitData() {
        state = state.copy(isLoading = true)

        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }

        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            val loginResult = loginRepository.loginUser(state.email, state.password)
            loginResult.fold(
                onSuccess = {
                    state = state.copy(isLoading = false)
                    validationEventChannel.send(ValidationEvent.Success(state.email))
                },
                onFailure = { error ->
                    if (error.message?.contains("Email not verified. Verification email sent.") == true) {
                        validationEventChannel.send(ValidationEvent.EmailNotVerified(state.email))
                    }
                    state = state.copy(
                        isLoading = false,
                        emailError = emailResult.errorMessage,
                        passwordError = error.message
                    )
                }
            )
        }
    }

    /**
     * Inicia sesión con la cuenta de Google.
     *
     * @param activity Actividad desde la cual se lanza el flujo de Google Sign-In.
     */
    private fun googleSignIn(activity: Activity) {
        viewModelScope.launch {
            state = state.copy(isGoogleLoading = true)

            val result = loginRepository.googleSignIn(activity)

            state = state.copy(isGoogleLoading = false)

            if (result.isSuccess) {
                validationEventChannel.send(ValidationEvent.Success("Google user"))
            } else {
                val error = result.exceptionOrNull()?.localizedMessage ?: "Google sign-in failed"
                if (!error.contains("cancelled", ignoreCase = true)) {
                    validationEventChannel.send(ValidationEvent.Failure(error))
                }
            }
        }
    }

    sealed class ValidationEvent {
        data class Success(val email: String) : ValidationEvent()
        data class EmailNotVerified(val email: String) : ValidationEvent()
        data class Failure(val error: String) : ValidationEvent()
    }
}
