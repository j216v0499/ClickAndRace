package com.dearos.clickandrace.ui.screens.authScreens.signUp

/**
 * Representa el estado actual del formulario de registro.
 *
 * Esta clase almacena los valores introducidos por el usuario, así como posibles errores de validación
 * asociados a cada campo. También incluye un indicador de carga para mostrar una UI de progreso
 * durante la validación o el envío del formulario.
 *
 * @property name Nombre introducido por el usuario.
 * @property nameError Mensaje de error para el campo de nombre, si lo hay.
 * @property email Correo electrónico introducido por el usuario.
 * @property emailError Mensaje de error para el campo de email, si lo hay.
 * @property password Contraseña introducida por el usuario.
 * @property passwordError Mensaje de error para el campo de contraseña, si lo hay.
 * @property acceptedTerms Indica si el usuario ha aceptado los términos y condiciones.
 * @property termsError Mensaje de error relacionado con los términos, si no han sido aceptados.
 * @property isLoading Indica si se está procesando el envío del formulario.
 */
data class SignUpFormState(
    val name: String = "",
    val nameError: String? = null,

    val email: String = "",
    val emailError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val acceptedTerms: Boolean = false,
    val termsError: String? = null,

    val isLoading: Boolean = false
)
