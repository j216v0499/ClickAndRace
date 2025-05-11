package com.dearos.clickandrace.ui.screens.authScreens.login

/**
 * Representa el estado actual del formulario de login.
 *
 * Esta clase se utiliza para almacenar los datos del usuario, los errores de validación
 * y los indicadores de carga mientras se realiza la autenticación o el inicio de sesión con Google.
 *
 * @property email Dirección de correo electrónico introducida por el usuario.
 * @property emailError Mensaje de error asociado al campo de correo electrónico, si existe.
 * @property password Contraseña introducida por el usuario.
 * @property passwordError Mensaje de error asociado al campo de contraseña, si existe.
 * @property isLoading Indica si se está procesando la solicitud de inicio de sesión (login tradicional).
 * @property isGoogleLoading Indica si se está procesando la autenticación mediante Google Sign-In.
 */
data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false
)
