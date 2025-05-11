package com.dearos.clickandrace.auth.domain.appRepository

import com.dearos.clickandrace.data.model.Transaction
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val supabaseClient: SupabaseClient) {

    /** 1. Crear transacción y devolver el objeto insertado */
    suspend fun createTransaction(tx: Transaction): Result<Transaction> = withContext(Dispatchers.IO) {
        return@withContext try {
            val inserted: Map<String, Any> = supabaseClient
                .from("transactions")
                .insert(mapOf(
                    "buyer_id"   to tx.buyerId,
                    "seller_id"  to tx.sellerId,
                    "product_id" to tx.productId,
                    "status"     to tx.status
                ))
                .decodeSingle()  // El DSL ya realiza internamente un SELECT tras el INSERT:contentReference[oaicite:2]{index=2}

            Result.success(inserted.toTransaction())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 2a. Leer por ID */
    suspend fun getTransactionById(id: String): Result<Transaction> = withContext(Dispatchers.IO) {
        return@withContext try {
            val row: Map<String, Any> = supabaseClient
                .from("transactions")
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle()  // decodeSingle<T>() convierte JSON→objeto:contentReference[oaicite:3]{index=3}

            Result.success(row.toTransaction())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 2b. Leer por estado */
    suspend fun getTransactionsByStatus(status: String): Result<List<Transaction>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val rows: List<Map<String, Any>> = supabaseClient
                .from("transactions")
                .select {
                    filter { eq("status", status) }
                }
                .decodeList()  // decodeList<T>() para listas de objetos JSON:contentReference[oaicite:4]{index=4}

            Result.success(rows.map { it.toTransaction() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 2c. Leer por usuario (comprador o vendedor) */
    suspend fun getTransactionsByUserId(userId: String): Result<List<Transaction>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val rows: List<Map<String, Any>> = supabaseClient
                .from("transactions")
                .select {
                    filter {
                        or {        // combinar múltiples condiciones con OR dentro de filter:contentReference[oaicite:5]{index=5}:contentReference[oaicite:6]{index=6}
                            eq("buyer_id", userId)
                            eq("seller_id", userId)
                        }
                    }
                }
                .decodeList()

            Result.success(rows.map { it.toTransaction() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 3. Actualizar estado de la transacción */
    suspend fun updateTransactionStatus(txId: String, newStatus: String): Result<Transaction> = withContext(Dispatchers.IO) {
        return@withContext try {
            val updated: Map<String, Any> = supabaseClient
                .from("transactions")
                .update(mapOf("status" to newStatus)) {
                    filter { eq("id", txId) }
                }
                .decodeSingle()  // decodeSingle<T>() tras UPDATE con DSL integrado
            Result.success(updated.toTransaction())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 4. Eliminar transacción por ID */
    suspend fun deleteTransaction(txId: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val deleted: List<Map<String, Any>> = supabaseClient
                .from("transactions")
                .delete {
                    filter { eq("id", txId) }
                }
                .decodeList()  // el DSL devuelve lista de objetos eliminados

            if (deleted.isNotEmpty()) Result.success(Unit)
            else Result.failure(Exception("Transacción no encontrada"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Conversión interna de mapa JSON a modelo */
    private fun Map<String, Any?>.toTransaction(): Transaction = Transaction(
        id         = this["id"] as? String,
        buyerId    = this["buyer_id"] as String,
        sellerId   = this["seller_id"] as String,
        productId  = this["product_id"] as String,
        status     = this["status"] as String,
        createdAt  = this["created_at"] as? String
    )
}
