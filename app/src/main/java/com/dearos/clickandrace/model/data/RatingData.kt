package com.dearos.clickandrace.model.data

import kotlinx.serialization.Serializable

/**
 * Representa una valoración realizada por un usuario hacia otro.
 *
 * @property id Identificador de la valoración.
 * @property rater_id ID del usuario que realiza la valoración.
 * @property rated_user_id ID del usuario que es valorado.
 * @property rating Valor numérico de la valoración.
 * @property comment Comentario adicional.
 * @property timestamp Fecha y hora de la valoración.
 */
@Serializable
data class RatingData(
    val id: String,
    val rater_id: String,
    val rated_user_id: String,
    val rating: Int,
    val comment: String,
    val timestamp: String
)