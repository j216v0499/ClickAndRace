package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.setNew


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.authRepository.SetPasswordRepo
import com.dearos.clickandrace.auth.domain.use_case.ValidatePassword
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de manejar la lógica para establecer una nueva contraseña
 * en el proceso de recuperación de contraseña.
 *
 * @property validatePassword Caso de uso para validar la contraseña.
 * @property repository Repositorio para actualizar la contraseña en el backend.
 * @property supabaseClient Cliente Supabase para autenticación y operaciones relacionadas.
 */
class SetPasswordViewModel(
    private val validatePassword: ValidatePassword,
    private val repository: SetPasswordRepo,
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    // Estado mutable del formulario que se expone como flujo inmutable
    private val _state = MutableStateFlow(SetPasswordFormState())
    val state = _state.asStateFlow()

    // Canal para enviar eventos de validación (éxito o error)
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Maneja los eventos del formulario.
     *
     * @param event Evento del formulario recibido.
     */
    fun onEvent(event: SetPasswordFormEvent) {
        when (event) {
            is SetPasswordFormEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }
            is SetPasswordFormEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword) }
            }
            is SetPasswordFormEvent.Submit -> {
                submitData()
            }
        }
    }

    /**
     * Valida y envía la nueva contraseña para su actualización.
     *
     * Realiza validaciones locales antes de llamar al repositorio,
     * actualiza el estado de carga y maneja el resultado del proceso.
     */
    private fun submitData() {
        _state.update { it.copy(isLoading = true) }

        val state = _state.value

        // Validación de la contraseña principal
        val passwordResult = validatePassword.execute(state.password)
        // Validar que la confirmación coincida con la contraseña
        val confirmPasswordResult = (state.password == state.confirmPassword)

        // Si la contraseña no es válida, actualizar el estado con el error y salir
        if (!passwordResult.successful) {
            _state.update {
                it.copy(
                    passwordError = passwordResult.errorMessage,
                    isLoading = false
                )
            }
            return
        }

        // Si las contraseñas no coinciden, actualizar el estado con el error y salir
        if (!confirmPasswordResult) {
            _state.update {
                it.copy(
                    confirmPasswordError = "Passwords do not match",
                    isLoading = false
                )
            }
            return
        }

        // Lanzar corrutina para actualizar la contraseña en el repositorio (backend)
        viewModelScope.launch {
            try {
                val updateSuccess = repository.updatePassword(state.password)

                if (updateSuccess.isSuccess) {
                    // Si la actualización fue exitosa, cerrar sesión del usuario inmediatamente
                    supabaseClient.auth.signOut()
                    // Enviar evento de éxito para navegar a la pantalla de login
                    validationEventChannel.send(ValidationEvent.Success)
                } else {
                    // En caso de error, actualizar el estado con el mensaje de error
                    val error = updateSuccess.exceptionOrNull()?.message ?: "Unknown error"
                    _state.update {
                        it.copy(
                            isLoading = false,
                            passwordError = error,
                            confirmPasswordError = null,
                        )
                    }
                }
            } catch (e: Exception) {
                // Manejo de excepciones inesperadas
                _state.update {
                    it.copy(
                        passwordError = "An error occurred:- ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Eventos de validación que el ViewModel puede emitir,
     * como el éxito en el establecimiento de la contraseña.
     */
    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}
