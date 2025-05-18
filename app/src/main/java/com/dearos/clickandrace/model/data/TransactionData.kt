package com.dearos.clickandrace.model.data

import kotlinx.serialization.Serializable

/**
 * Representa una transacción entre un comprador y un vendedor.
 *
 * @property id Identificador único de la transacción.
 * @property product_id ID del producto transaccionado.
 * @property buyer_id ID del comprador.
 * @property seller_id ID del vendedor.
 * @property status Estado de la transacción.
 * @property timestamp Fecha y hora de la transacción.
 */
@Serializable
data class TransactionData(
    val id: String,
    val product_id: String,
    val buyer_id: String,
    val seller_id: String,
    val status: String,
    val timestamp: String
)
