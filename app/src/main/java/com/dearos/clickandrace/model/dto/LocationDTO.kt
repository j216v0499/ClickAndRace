package com.dearos.clickandrace.model.dto

import kotlinx.serialization.Serializable

/**
 * DTO que representa una ubicación geográfica en la aplicación.
 * Puede usarse para productos, zonas, puntos de interés, etc.
 *
 * @param name Nombre descriptivo de la ubicación (por ejemplo, "Madrid Centro").
 * @param latitude Coordenada de latitud en formato decimal (ej. 40.4168).
 * @param longitude Coordenada de longitud en formato decimal (ej. -3.7038).
 * @param type Tipo de ubicación, por defecto es "Default".
 *
 */
@Serializable
data class LocationDTO(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val type: String? = "Default"
)
