package com.dearos.clickandrace.data.model
/*
class Rating {
}*/


data class Rating(
    val id: String? = null,         // UUID generado por Supabase
    val userId: String,             // ID del usuario que recibe la valoración
    val raterId: String,            // ID del usuario que da la valoración
    val rating: Float,              // Valoración (1-5)
    val comment: String? = null,    // Comentario opcional
    val createdAt: String? = null   // Fecha de creación (generada por Supabase)
)