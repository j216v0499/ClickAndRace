package com.dearos.clickandrace.ui.screens.authScreens.signUp

/**
 * Representa los diferentes eventos posibles del formulario de registro.
 *
 * Esta clase sellada (sealed) define todos los tipos de interacciones que el usuario
 * puede realizar en el formulario, tales como cambiar el nombre, correo, contraseña,
 * aceptar los términos y enviar el formulario.
 */
sealed class SignUpFormEvent {

    /**
     * Evento que se lanza cuando el usuario cambia el campo del nombre.
     *
     * @param name El nuevo valor del nombre introducido.
     */
    data class NameChanged(val name: String) : SignUpFormEvent()

    /**
     * Evento que se lanza cuando el usuario cambia el campo del email.
     *
     * @param email El nuevo valor del correo electrónico introducido.
     */
    data class EmailChanged(val email: String) : SignUpFormEvent()

    /**
     * Evento que se lanza cuando el usuario cambia el campo de la contraseña.
     *
     * @param password La nueva contraseña introducida por el usuario.
     */
    data class PasswordChanged(val password: String) : SignUpFormEvent()

    /**
     * Evento que se lanza cuando el usuario marca o desmarca la aceptación de términos.
     *
     * @param isAccepted Booleano que indica si se han aceptado los términos y condiciones.
     */
    data class AcceptTerms(val isAccepted: Boolean) : SignUpFormEvent()

    /**
     * Evento que representa la acción de enviar el formulario completo.
     * Este evento debería validar todos los campos antes de enviar la información.
     */
    object Submit : SignUpFormEvent()
}
