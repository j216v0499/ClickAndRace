package com.dearos.clickandrace.data.model
//
//class Transaction {
//}

data class Transaction(
    val id: String? = null,                     // UUID generado por Supabase
    val buyerId: String,                        // ID del comprador
    val sellerId: String,                       // ID del vendedor
    val productId: String,                      // ID del producto involucrado
    val status: String,         // Estado de la transacción (pendiente, completada, cancelada)
    val createdAt: String? = null // Fecha de creación (generada por Supabase)
)