package com.dearos.clickandrace.ui.screens.authScreens.signUp

import com.dearos.clickandrace.auth.domain.authRepository.SignUpRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.use_case.ValidateEmail
import com.dearos.clickandrace.auth.domain.use_case.ValidateName
import com.dearos.clickandrace.auth.domain.use_case.ValidatePassword
import com.dearos.clickandrace.auth.domain.use_case.ValidateTerms
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el estado y la lógica del formulario de registro de usuario.
 * Se encarga de validar los campos, comunicarse con el repositorio y manejar eventos de UI.
 *
 * @param validateEmail Caso de uso para validar emails.
 * @param validatePassword Caso de uso para validar contraseñas.
 * @param validateTerms Caso de uso para validar la aceptación de términos y condiciones.
 * @param validateName Caso de uso para validar nombres.
 * @param repository Repositorio encargado de las operaciones de registro y verificación de usuario.
 */
class SignUpViewModel(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateTerms: ValidateTerms,
    private val validateName: ValidateName,
    private val repository: SignUpRepo
) : ViewModel() {

    /** Estado del formulario mantenido en memoria y expuesto como flujo inmutable. */
    private val _state = MutableStateFlow(SignUpFormState())
    val stateFlow = _state.asStateFlow()

    /** Canal que emite eventos de validación, como éxito o errores, hacia la interfaz. */
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Manejador de eventos del formulario de registro.
     * Actualiza el estado correspondiente según el tipo de evento recibido.
     *
     * @param event Evento lanzado desde la interfaz de usuario.
     */
    fun onEvent(event: SignUpFormEvent) {
        when (event) {
            is SignUpFormEvent.NameChanged -> {
                _state.update { it.copy(name = event.name) }
            }
            is SignUpFormEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }
            is SignUpFormEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }
            is SignUpFormEvent.AcceptTerms -> {
                _state.update { it.copy(acceptedTerms = event.isAccepted) }
            }
            is SignUpFormEvent.Submit -> {
                submitData()
            }
        }
    }

    /**
     * Ejecuta la lógica de validación y registro cuando el usuario envía el formulario.
     * - Valida cada campo del formulario.
     * - Muestra errores si los hay.
     * - Verifica si el correo ya existe.
     * - Si no existe, procede con el registro y emite evento de éxito.
     */
    private fun submitData() {
        // Mostrar spinner de carga
        _state.update { it.copy(isLoading = true) }

        // Obtener estado actual del formulario
        val state = _state.value

        // Validaciones individuales
        val nameResult = validateName.execute(state.name)
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val termsResult = validateTerms.execute(state.acceptedTerms)




        // Verificación de errores en las validaciones
        val hasError = listOf(
            emailResult,
            passwordResult,
            termsResult,
            nameResult
        ).any { !it.successful }

        // Si hay errores, actualizamos el estado con los mensajes correspondientes
        if (hasError) {
            _state.update {
                it.copy(
                    nameError = nameResult.errorMessage,
                    emailError = emailResult.errorMessage,
                    passwordError = passwordResult.errorMessage,
                    termsError = termsResult.errorMessage,
                    isLoading = false
                )
            }
            return
        }

        // Lanzamos corrutina para proceso de red asincrónico
        viewModelScope.launch {
            try {
                // Verificamos si el email ya existe en la tabla public.users
                val userExists = repository.isEmailInPublicUsersTable(state.email)

                if (userExists) {
                    // Caso 1: el email ya existe -> mostramos error
                    _state.update {
                        it.copy(
                            nameError = nameResult.errorMessage,
                            emailError = "Email already exists.",
                            passwordError = passwordResult.errorMessage,
                            termsError = termsResult.errorMessage,
                            isLoading = false
                        )
                    }
                } else {
                    // Caso 2: email nuevo -> proceder con el registro y enviar a verificación OTP
                    val signUpResult = repository.signUp(
                        email = state.email,
                        password = state.password,
                        fullName = state.name
                    )

                    if (signUpResult.isSuccess) {
                        _state.update { it.copy(isLoading = false) }
                        // Emite evento de éxito que puede ser observado desde la UI
                        validationEventChannel.send(ValidationEvent.Success(state.email))
                    } else {
                        // Fallo en el registro: mostramos el mensaje de error
                        val error = signUpResult.exceptionOrNull()?.message ?: "Unknown error"
                        _state.update { it.copy(isLoading = false, emailError = error) }
                    }
                }
            } catch (e: Exception) {
                // Error inesperado (ej: red)
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
     * Eventos de validación que se comunican a la capa de presentación (UI).
     */
    sealed class ValidationEvent {
        /**
         * Evento que indica que el formulario fue enviado con éxito.
         *
         * @param email Email del usuario registrado.
         */
        data class Success(val email: String) : ValidationEvent()
    }
}
