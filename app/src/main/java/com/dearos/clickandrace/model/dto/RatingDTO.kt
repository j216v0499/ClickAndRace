package com.dearos.clickandrace.model.dto

import kotlinx.serialization.Serializable

/**
 * DTO para enviar una calificación de un usuario a otro.
 *
 * @param rater_id ID del usuario que realiza la calificación (normalmente el comprador).
 * @param user_id ID del usuario calificado (normalmente el vendedor).
 * @param rating Valor numérico de la calificación (por ejemplo, entre 1 y 5).
 * @param comment Comentario adicional sobre la experiencia (opcional).
 */
@Serializable
data class RatingDTO(
    val rater_id: String,
    val user_id: String,
    val rating: Int,
    val comment: String
)
