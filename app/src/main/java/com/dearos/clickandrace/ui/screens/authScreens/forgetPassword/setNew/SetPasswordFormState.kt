package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.setNew


/**
 * Estado del formulario para establecer una nueva contraseña.
 *
 * @property password Contraseña ingresada por el usuario.
 * @property passwordError Mensaje de error relacionado con la contraseña, si existe.
 * @property confirmPassword Contraseña de confirmación ingresada por el usuario.
 * @property confirmPasswordError Mensaje de error relacionado con la confirmación de contraseña, si existe.
 * @property isLoading Indica si el formulario está en proceso de envío o carga.
 */
data class SetPasswordFormState(
    val password: String = "",
    val passwordError: String? = null,

    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,

    val isLoading: Boolean = false
)
