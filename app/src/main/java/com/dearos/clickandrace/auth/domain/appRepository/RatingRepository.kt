package com.dearos.clickandrace.auth.domain.appRepository

import com.dearos.clickandrace.data.model.Rating
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RatingRepository(private val supabaseClient: SupabaseClient) {

    /** 1. Crear una valoración y devolver el objeto insertado */
    suspend fun createRating(rating: Rating): Result<Rating> = withContext(Dispatchers.IO) {
        return@withContext try {
            val inserted: Map<String, Any> = supabaseClient
                .from("ratings")
                .insert(mapOf(
                    "user_id"   to rating.userId,
                    "rater_id"  to rating.raterId,
                    "rating"    to rating.rating,
                    "comment"   to rating.comment
                ))
                .decodeSingle()

            Result.success(inserted.toRating())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 2a. Leer una valoración por su ID */
    suspend fun getRatingById(id: String): Result<Rating> = withContext(Dispatchers.IO) {
        return@withContext try {
            val row: Map<String, Any> = supabaseClient
                .from("ratings")
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle()

            Result.success(row.toRating())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 2b. Leer todas las valoraciones de un usuario */
    suspend fun getRatingsByUserId(userId: String): Result<List<Rating>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val rows: List<Map<String, Any>> = supabaseClient
                .from("ratings")
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeList()

            Result.success(rows.map { it.toRating() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 3. Actualizar campos de una valoración existente */
    suspend fun updateRating(
        id: String,
        updatedFields: Map<String, Any>
    ): Result<Rating> = withContext(Dispatchers.IO) {
        return@withContext try {
            val updated: Map<String, Any> = supabaseClient
                .from("ratings")
                .update(updatedFields) {
                    filter { eq("id", id) }
                }
                .decodeSingle()

            Result.success(updated.toRating())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** 4. Eliminar una valoración por ID */
    suspend fun deleteRating(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val deleted: List<Map<String, Any>> = supabaseClient
                .from("ratings")
                .delete {
                    filter { eq("id", id) }
                }
                .decodeList()

            if (deleted.isNotEmpty()) Result.success(Unit)
            else Result.failure(Exception("Rating no encontrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Helper: convierte el mapa JSON a tu modelo `Rating` */
    private fun Map<String, Any?>.toRating(): Rating = Rating(
        id         = this["id"] as? String,
        userId     = this["user_id"] as String,
        raterId    = this["rater_id"] as String,
        rating     = (this["rating"] as? Number)?.toFloat() ?: 0f,
        comment    = this["comment"] as? String,
        createdAt  = this["created_at"] as? String
    )
}
