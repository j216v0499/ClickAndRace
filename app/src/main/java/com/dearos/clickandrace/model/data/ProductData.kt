package com.dearos.clickandrace.model.data

import kotlinx.serialization.Serializable

/**
 * Representa un producto publicado por un usuario.
 *
 * @property id Identificador del producto.
 * @property user_id Identificador del usuario que lo ofrece.
 * @property title Título del producto.
 * @property description Descripción del producto.
 * @property price Precio del producto.
 * @property type Tipo del producto.
 * @property location Ubicación del producto.
 * @property availability Días de disponibilidad del producto.
 * @property product_picture Lista opcional de URLs de imágenes del producto.
 */
@Serializable
data class ProductData(
    val id: String,
    val user_id: String,
    val title: String,
    val description: String,
    val price: Double,
    val type: String,
    val location: String,
    val availability: Map<String, ProductAvailabilityDay>,
    val product_picture: List<String>? = null,

    )