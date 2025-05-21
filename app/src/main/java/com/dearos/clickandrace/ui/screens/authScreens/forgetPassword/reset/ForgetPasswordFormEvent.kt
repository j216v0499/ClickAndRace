package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.reset

/**
 * Representa los diferentes eventos que pueden ocurrir en el formulario de "Olvidé mi contraseña".
 * Estos eventos son procesados por la ViewModel para actualizar el estado o ejecutar acciones.
 */
sealed class ForgetPasswordFormEvent {

    /**
     * Evento que indica que el usuario ha cambiado el valor del campo de email.
     *
     * @param email El nuevo valor del campo de correo electrónico.
     */
    data class EmailChanged(val email: String) : ForgetPasswordFormEvent()

    /**
     * Evento que indica que el usuario ha enviado el formulario para restablecer la contraseña.
     */
    object Submit : ForgetPasswordFormEvent()
}
