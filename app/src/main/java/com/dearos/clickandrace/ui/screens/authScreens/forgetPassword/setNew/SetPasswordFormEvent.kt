package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.setNew

/**
 * Eventos que maneja el formulario de establecer nueva contraseña.
 */
sealed class SetPasswordFormEvent {

    /**
     * Evento que se dispara cuando cambia el valor de la contraseña.
     * @param password La nueva contraseña ingresada.
     */
    data class PasswordChanged(val password: String) : SetPasswordFormEvent()

    /**
     * Evento que se dispara cuando cambia el valor de la confirmación de contraseña.
     * @param confirmPassword La nueva contraseña de confirmación ingresada.
     */
    data class ConfirmPasswordChanged(val confirmPassword: String) : SetPasswordFormEvent()

    /**
     * Evento que se dispara cuando el usuario envía el formulario para establecer la contraseña.
     */
    object Submit : SetPasswordFormEvent()
}
