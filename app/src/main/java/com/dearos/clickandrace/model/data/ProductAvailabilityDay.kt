package com.dearos.clickandrace.model.data

import kotlinx.serialization.Serializable

/**
 * Representa la disponibilidad de un producto en un día específico.
 *
 * @property start Hora de inicio de disponibilidad (formato string).
 * @property end Hora de fin de disponibilidad (formato string).
 */
@Serializable
data class ProductAvailabilityDay(
    val start: String,
    val end: String
)
