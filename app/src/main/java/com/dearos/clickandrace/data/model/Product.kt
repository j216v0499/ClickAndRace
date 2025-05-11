package com.dearos.clickandrace.data.model
/*
class Product {
}*/

data class Product(
    val id: String? = null,                     // UUID generado por Supabase
    val userId: String,                         // ID del usuario que publica el producto
    val title: String,                          // Título del producto
    val description: String,                    // Descripción detallada
    val price: Float,                           // Precio del producto
    val type: String,                           // Tipo de vehículo (coche, moto, agrícola)
    val location: String,                       // Ubicación geográfica (formato JSON como string)
    val availability: Map<String, Any>? = null, // Horario de disponibilidad (formato JSON)
    val createdAt: String? = null               // Fecha de creación (generada por Supabase)
)