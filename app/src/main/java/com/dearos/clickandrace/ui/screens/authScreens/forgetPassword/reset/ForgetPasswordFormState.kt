package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.reset

/**
 * Estado que representa los datos y la UI del formulario de "Olvidé mi contraseña".
 *
 * @property email El valor actual del campo de correo electrónico introducido por el usuario.
 * @property emailError Mensaje de error relacionado con el email, si existe.
 * @property isLoading Indica si el formulario está en proceso de envío / carga.
 */
data class ForgetPasswordFormState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false
)
