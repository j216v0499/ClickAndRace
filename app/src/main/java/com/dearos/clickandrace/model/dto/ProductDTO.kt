package com.dearos.clickandrace.model.dto

import com.dearos.clickandrace.model.data.ProductAvailabilityDay
import kotlinx.serialization.Serializable

/**
 * DTO que representa un producto publicado en la plataforma.
 *
 * @param user_id ID del usuario que publica el producto.
 * @param title Título del producto.
 * @param description Descripción detallada del producto.
 * @param price Precio del producto (en euros, dólares, etc.).
 * @param type Tipo de producto (por ejemplo: "venta", "alquiler").
 * @param location Ubicación geográfica o dirección del producto.
 * @param availability Mapa de disponibilidad por días (usando objetos ProductAvailabilityDay).
 * @param product_picture Lista de URLs o paths de las imágenes del producto (opcional).
 */
@Serializable
data class ProductDTO(
    val user_id: String,
    val title: String,
    val description: String,
    val price: Double,
    val type: String,
    val location: String,
    val availability: Map<String, ProductAvailabilityDay>,
    val product_picture: List<String>? = null
)
