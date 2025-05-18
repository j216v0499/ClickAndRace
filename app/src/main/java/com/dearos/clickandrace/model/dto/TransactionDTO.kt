package com.dearos.clickandrace.model.dto

import kotlinx.serialization.Serializable

/**
 * DTO que representa una transacción entre dos usuarios.
 *
 * @param product_id ID del producto implicado en la transacción.
 * @param buyer_id ID del comprador.
 * @param seller_id ID del vendedor.
 * @param status Estado actual de la transacción (por ejemplo: "pendiente", "aceptada", "finalizada").
 */
@Serializable
data class TransactionDTO(
    val product_id: String,
    val buyer_id: String,
    val seller_id: String,
    val status: String,
)
