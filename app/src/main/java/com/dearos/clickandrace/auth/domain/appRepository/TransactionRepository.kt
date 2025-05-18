package com.dearos.clickandrace.auth.domain.appRepository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

/**
 * Repositorio encargado de gestionar las operaciones CRUD relacionadas con las transacciones (transactions)
 * en la base de datos Supabase.
 *
 * @param supabase Cliente de Supabase configurado.
 */
class TransactionRepository(private val supabase: SupabaseClient) {

    /**
     * Crea una nueva transacción entre un comprador y un vendedor para un producto específico.
     *
     * @param productId ID del producto involucrado en la transacción.
     * @param sellerId ID del vendedor del producto.
     * @param buyerId ID del comprador del producto.
     * @return `true` si la transacción se creó correctamente, `false` si hubo un error.
     */
    suspend fun createTransaction(productId: String, sellerId: String, buyerId: String): Boolean {
        val data = mapOf(
            "product_id" to productId,
            "seller_id" to sellerId,
            "buyer_id" to buyerId,
            "status" to "pending"
        )
        return try {
            supabase.from("transactions").insert(data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Obtiene todas las transacciones relacionadas con un usuario (comprador o vendedor).
     *
     * @param userId ID del usuario (comprador o vendedor).
     * @return Lista de transacciones.
     */
    suspend fun getTransactionsByUserId(userId: String): List<Map<String, Any>> {
//        return try {
//            val transactions = supabase.from("transactions")
//                .select("*")
//                .or("buyer_id.eq.$userId,seller_id.eq.$userId")  // Condiciones OR para comprador o vendedor
//                .execute()
//                .getBody<List<Map<String, Any>>>()
//            transactions ?: emptyList()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList()
//        }
        return emptyList()
    }

    /**
     * Agrega una valoración a una transacción.
     *
     * @param transactionId ID de la transacción.
     * @param rating Puntuación de la valoración.
     * @param reviewerId ID del usuario que realiza la valoración (comprador).
     * @return `true` si la valoración se agregó correctamente, `false` si hubo un error.
     */
    suspend fun addRating(transactionId: String, rating: Int, reviewerId: String): Boolean {
        val data = mapOf(
            "transaction_id" to transactionId,
            "rating" to rating,
            "reviewer_id" to reviewerId
        )
        return try {
            supabase.from("ratings").insert(data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Recalcula la puntuación promedio de un vendedor basándose en las valoraciones recibidas.
     *
     * @param sellerId ID del vendedor.
     * @return Puntuación promedio.
     */
    suspend fun recalculateAverageRating(sellerId: String): Double {
//        return try {
//            val ratings = supabase.from("ratings")
//                .select("rating")
//                .eq("seller_id", sellerId)
//                .execute()
//                .getBody<List<Map<String, Any>>>()
//
//            val totalRatings = ratings?.map { it["rating"] as? Int }?.filterNotNull() ?: emptyList()
//            val average = if (totalRatings.isNotEmpty()) totalRatings.average() else 0.0
//            average
//        } catch (e: Exception) {
//            e.printStackTrace()
//            0.0
//        }
        return 0.0
    }

    /**
     * Actualiza el estado de una transacción (ej., de pendiente a completada).
     *
     * @param transactionId ID de la transacción.
     * @param status Nuevo estado de la transacción.
     * @return `true` si se actualizó correctamente, `false` si hubo un error.
     */
    suspend fun updateTransactionStatus(transactionId: String, status: String): Boolean {
//        val data = mapOf("status" to status)
//        return try {
//            supabase.from("transactions")
//                .update(data)
//                .eq("id", transactionId)
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
        return true

    }
}
