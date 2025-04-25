package com.dearos.clickandrace.auth.domain.use_case

/**
 * Clase encargada de validar el nombre del usuario.
 * Verifica que el nombre no esté vacío, tenga la longitud adecuada, no contenga caracteres especiales
 * y no esté en la lista de nombres prohibidos.
 */
class ValidateName {
    /**
     * Ejecuta la validación del nombre.
     * @param name Nombre a validar.
     * @return Resultado de la validación con información sobre el éxito o el error.
     */
    fun execute(name: String): ResultValidation {
        // Recorta los espacios al principio y final.
        val trimmedName = name.trim()

        // Verifica si el nombre está vacío.
        if (trimmedName.isBlank()) {
            return ResultValidation(
                successful = false,
                errorMessage = "Name can't be blank or just spaces."
            )
        }

        // Verifica que la longitud del nombre esté entre 2 y 50 caracteres.
        if (trimmedName.length < 2) {
            return ResultValidation(
                successful = false,
                errorMessage = "The name must be at least 2 characters long"
            )
        }

        if (trimmedName.length > 50) {
            return ResultValidation(
                successful = false,
                errorMessage = "The name must not exceed 50 characters"
            )
        }

        // Verifica que el nombre no contenga caracteres inválidos.
        val invalidSymbolRegex = Regex("[^a-zA-Z\\s0-9]")
        if (invalidSymbolRegex.containsMatchIn(trimmedName)) {
            return ResultValidation(
                successful = false,
                errorMessage = "The name must not contain invalid symbols"
            )
        }

        // Verifica si el nombre está en la lista de nombres prohibidos.
        val forbiddenNames = listOf("admin", "root", "null")
        if (trimmedName.lowercase() in forbiddenNames) {
            return ResultValidation(
                successful = false,
                errorMessage = "The name is not allowed"
            )
        }

        // Verifica que el nombre no contenga números.
        if (trimmedName.any { it.isDigit() }) {
            return ResultValidation(
                successful = false,
                errorMessage = "The name must not contain numbers"
            )
        }

        // Si pasa todas las verificaciones, el nombre es válido.
        return ResultValidation(
            successful = true
        )
    }
}
