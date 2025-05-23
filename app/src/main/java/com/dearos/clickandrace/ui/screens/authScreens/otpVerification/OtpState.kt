package com.dearos.clickandrace.ui.screens.authScreens.otpVerification

/**
 * Clase que representa el estado de la pantalla de verificación OTP (One-Time Password).
 * Este estado se usa para controlar los valores introducidos por el usuario, el enfoque actual,
 * y la validación del código.
 *
 * @property code Lista de dígitos del código OTP. Cada entrada puede ser `null` si aún no se ha introducido.
 *                Por defecto, contiene 6 posiciones vacías (null).
 * @property focusedIndex Índice del campo actualmente enfocado. Se utiliza para mover el foco entre campos.
 * @property isValid Indica si el código OTP completo es válido (`true`), inválido (`false`) o aún no evaluado (`null`).
 */

data class OtpState(
    val code: List<Int?> = (1..6).map { null },    // Lista de 6 dígitos inicializados como null
    val focusedIndex: Int? = null,                // Índice del campo actualmente enfocado
    val isValid: Boolean? = null                  // Resultado de la validación del OTP
)
