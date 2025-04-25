package com.dearos.clickandrace.auth.domain.use_case

/**
 * Clase que representa el resultado de una validación.
 * Contiene un indicador de éxito (`successful`) y un mensaje de error (si aplica).
 */
data class ResultValidation(
    val successful: Boolean,
    val errorMessage: String? = null
)
