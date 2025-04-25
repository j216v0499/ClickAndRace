package com.dearos.clickandrace.auth.domain.use_case

/**
 * Clase encargada de verificar si el usuario ha aceptado los términos y condiciones.
 */
class ValidateTerms {
    /**
     * Ejecuta la validación de los términos de uso.
     * @param acceptedTerms Booleano que indica si el usuario aceptó los términos.
     * @return Resultado de la validación con éxito o error.
     */
    fun execute(acceptedTerms: Boolean): ResultValidation {
        if (!acceptedTerms) {
            return ResultValidation(
                successful = false,
                errorMessage = "Please accept the terms"
            )
        }
        return ResultValidation(successful = true)
    }
}
