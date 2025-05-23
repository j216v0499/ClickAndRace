package com.dearos.clickandrace.ui.screens.authScreens.otpVerification

/**
 * Representa las diferentes acciones que pueden ocurrir durante la verificación del OTP.
 */
sealed interface OtpAction {

    /**
     * Acción que se dispara cuando el usuario ingresa un número en uno de los campos del OTP.
     *
     * @property number El número ingresado por el usuario (puede ser nulo si se elimina).
     * @property index El índice del campo donde se ingresó el número (0 a 3).
     */
    data class OnEnterNumber(
        val number: Int?,
        val index: Int
    ) : OtpAction

    /**
     * Acción que se dispara cuando el enfoque (focus) cambia a un campo diferente del OTP.
     *
     * @property index El índice del nuevo campo enfocado.
     */
    data class OnChangeFieldFocused(
        val index: Int
    ) : OtpAction

    /**
     * Acción que se dispara cuando el usuario presiona el botón de retroceso en el teclado.
     */
    data object OnKeyboardBack : OtpAction
}
