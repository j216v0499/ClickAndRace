package com.dearos.clickandrace.model.data

import kotlinx.serialization.Serializable

/**
 * Representa una ubicación geográfica con un tipo opcional.
 *
 * @property id Identificador único de la ubicación.
 * @property user_id Identificador del usuario que añade la ubicacion.
 * @property name Nombre de la ubicación.
 * @property latitude Latitud en coordenadas geográficas.
 * @property longitude Longitud en coordenadas geográficas.
 * @property type Tipo de ubicación (por defecto "Default").
 */

@Serializable
data class LocationData(
    val id: String,
    val user_id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val type: String? = "Default"
)
