package com.dearos.clickandrace.auth.domain.use_case

/**
 * Clase encargada de validar la contraseña.
 * Verifica que la contraseña tenga al menos 8 caracteres y contenga tanto letras como números.
 */
class ValidatePassword {
    /**
     * Ejecuta la validación de la contraseña.
     * @param password Contraseña a validar.
     * @return Resultado de la validación con éxito o error.
     */
    fun execute(password: String): ResultValidation {
        // Verifica que la longitud de la contraseña sea al menos 8 caracteres.
        if (password.length < 8) {
            return ResultValidation(
                successful = false,
                errorMessage = "Password must be at least 8 characters long"
            )
        }

        // Verifica que la contraseña contenga al menos una letra y un número.
        val containsLetters = password.any { it.isLetter() }
        val containsDigits = password.any { it.isDigit() }
        if (!containsLetters || !containsDigits) {
            return ResultValidation(
                successful = false,
                errorMessage = "Password must include a letter and a digit."
            )
        }

        // Si pasa todas las verificaciones, la contraseña es válida.
        return ResultValidation(successful = true)
    }
}
