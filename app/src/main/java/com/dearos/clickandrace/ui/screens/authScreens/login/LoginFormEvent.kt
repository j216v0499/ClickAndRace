package com.dearos.clickandrace.ui.screens.authScreens.login

import android.app.Activity

/**
 * Representa los diferentes eventos que pueden ocurrir en el formulario de login.
 *
 * Esta sealed class se utiliza para manejar las interacciones del usuario dentro de la vista,
 * como escribir el correo, la contraseña o enviar el formulario. También incluye el evento de
 * inicio de sesión con Google.
 */
sealed class LoginFormEvent {

    /**
     * Evento que se dispara cuando el usuario cambia el contenido del campo de correo electrónico.
     *
     * @property email El nuevo valor del correo electrónico introducido por el usuario.
     */
    data class EmailChanged(val email: String) : LoginFormEvent()

    /**
     * Evento que se dispara cuando el usuario cambia el contenido del campo de contraseña.
     *
     * @property password La nueva contraseña introducida por el usuario.
     */
    data class PasswordChanged(val password: String) : LoginFormEvent()

    /**
     * Evento que se dispara cuando el usuario pulsa el botón de enviar (login).
     * Inicia el proceso de validación y autenticación del usuario.
     */
    object Submit : LoginFormEvent()

    /**
     * Evento que se dispara cuando el usuario elige iniciar sesión con Google.
     *
     * @property activity La actividad actual desde la que se lanza el intent de autenticación con Google.
     */
    data class GoogleSignIn(val activity: Activity) : LoginFormEvent()
}
