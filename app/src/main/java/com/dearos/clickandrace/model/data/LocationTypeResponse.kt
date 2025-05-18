package com.dearos.clickandrace.model.data

import kotlinx.serialization.Serializable

/**
 * Representa la respuesta del tipo de una ubicación.
 *
 * @property type Tipo de la ubicación, puede ser nulo.
 */
@Serializable
data class LocationTypeResponse(val type: String?)

