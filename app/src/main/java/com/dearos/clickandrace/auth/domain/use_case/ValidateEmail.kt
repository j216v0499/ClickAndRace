package com.dearos.clickandrace.auth.domain.use_case


import android.util.Patterns

/**
 * Clase encargada de validar una dirección de correo electrónico.
 * Verifica que el correo no esté vacío, no exceda la longitud máxima permitida
 * y que cumpla con el formato estándar de una dirección de email.
 */
class ValidateEmail {
    /**
     * Ejecuta la validación del email.
     * @param email Correo electrónico a validar.
     * @return Resultado de la validación con información si fue exitosa o si hubo algún error.
     */
    fun execute(email: String): ResultValidation {
        // Elimina espacios en blanco al principio y al final.
        val trimmedEmail = email.trim()

        // Verifica si el correo está vacío.
        if (trimmedEmail.isBlank()) {
            return ResultValidation(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }

        // Verifica que la longitud del email no supere los 254 caracteres.
        if (email.trim().length > 254) {
            return ResultValidation(
                successful = false,
                errorMessage = "The email must not exceed 254 characters"
            )
        }

        // Utiliza el patrón de expresión regular para verificar si el email tiene un formato válido.
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return ResultValidation(
                successful = false,
                errorMessage = "Please enter a valid email"
            )
        }

        // Si pasa todas las verificaciones, el email es válido.
        return ResultValidation(successful = true)
    }
}
