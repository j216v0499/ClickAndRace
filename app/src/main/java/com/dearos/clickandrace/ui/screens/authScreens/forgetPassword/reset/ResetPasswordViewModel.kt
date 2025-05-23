package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.reset


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.authRepository.ResetPasswordRepo
import com.dearos.clickandrace.auth.domain.use_case.ValidateEmail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica de restablecimiento de contraseña.
 * Valida el email y gestiona el envío de OTP.
 *
 * @property validateEmail Caso de uso para validar el email.
 * @property repository Repositorio para operaciones relacionadas con restablecer contraseña.
 */
class ResetPasswordViewModel(
    private val validateEmail: ValidateEmail,
    private val repository: ResetPasswordRepo
) : ViewModel() {

    // Estado mutable privado con el estado del formulario
    private val _state = MutableStateFlow(ForgetPasswordFormState())
    // Exposición pública del estado como Flow inmutable
    val stateFlow = _state.asStateFlow()

    // Canal para enviar eventos de validación (ej: éxito en el envío OTP)
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Maneja los eventos del formulario.
     * @param event Evento que ocurre en el formulario.
     */
    fun onEvent(event: ForgetPasswordFormEvent) {
        when (event) {
            is ForgetPasswordFormEvent.EmailChanged -> {
                // Actualiza el estado con el email introducido
                _state.update { it.copy(email = event.email) }
            }
            is ForgetPasswordFormEvent.Submit -> {
                // Inicia el proceso de envío de OTP
                submitData()
            }
        }
    }

    /**
     * Lógica para enviar OTP tras validar el email.
     */
    private fun submitData() {
        // Indicamos que la operación está en curso
        _state.update { it.copy(isLoading = true) }

        val state = _state.value
        val emailResult = validateEmail.execute(state.email)

        // Validación de email; si falla se actualiza el error y se detiene el proceso
        if (!emailResult.successful) {
            _state.update {
                it.copy(
                    emailError = emailResult.errorMessage,
                    isLoading = false
                )
            }
            return
        }

        // Ejecutar operación asíncrona para enviar OTP
        viewModelScope.launch {
            try {
                // Intentar enviar OTP con el email proporcionado
                val otpSentResult = repository.sendOTP(_state.value.email)

                if (otpSentResult.isSuccess) {
                    // Si es exitoso, actualiza estado y envía evento de éxito para navegación
                    _state.update { it.copy(isLoading = false) }
                    validationEventChannel.send(ValidationEvent.Success(state.email))
                } else {
                    // En caso de fallo en el envío OTP, muestra error
                    _state.update {
                        it.copy(
                            emailError = "Failed to send OTP. Please try again.",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                // Captura excepciones inesperadas y actualiza estado con el error
                _state.update {
                    it.copy(
                        emailError = "An error occurred: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Método alternativo con lógica para verificar existencia de email en tabla pública.
     * Actualmente comentado o sin uso.
     */
    private fun submitData2() {
        _state.update { it.copy(isLoading = true) }

        val state = _state.value
        val emailResult = validateEmail.execute(state.email)

        if (!emailResult.successful) {
            _state.update {
                it.copy(
                    emailError = emailResult.errorMessage,
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                val userExists = repository.isEmailInPublicUsersTable(_state.value.email)
                if (!userExists) {
                    _state.update {
                        it.copy(
                            emailError = "Email do not exists.",
                            isLoading = false
                        )
                    }
                } else {
                    val otpSentResult = repository.sendOTP(_state.value.email)

                    if (otpSentResult.isSuccess) {
                        _state.update { it.copy(isLoading = false) }
                        validationEventChannel.send(ValidationEvent.Success(state.email))
                    } else {
                        _state.update {
                            it.copy(
                                emailError = "Failed to send OTP. Please try again.",
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        emailError = "An error occurred :- ${e.message} ",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Eventos que pueden emitir validaciones exitosas.
     */
    sealed class ValidationEvent {
        /**
         * Evento emitido cuando el OTP se envía con éxito.
         * @param email Email para el que se envió el OTP.
         */
        data class Success(val email: String) : ValidationEvent()
    }
}
