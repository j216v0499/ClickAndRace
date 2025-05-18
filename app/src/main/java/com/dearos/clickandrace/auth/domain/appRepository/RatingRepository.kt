package com.dearos.clickandrace.auth.domain.appRepository

import com.dearos.clickandrace.model.dto.RatingDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

/**
 * Repositorio encargado de gestionar las operaciones CRUD relacionadas con las valoraciones (ratings)
 * en la base de datos Supabase.
 *
 * Proporciona métodos para obtener, insertar y actualizar valoraciones de usuarios,
 * así como mantener actualizada la puntuación promedio del usuario en la base de datos.
 *
 * @param supabaseClient Cliente Supabase configurado.
 */
class RatingRepository(private val supabaseClient: SupabaseClient) {

    /**
     * Inserta una nueva valoración y actualiza la puntuación promedio del usuario valorado.
     *
     * @param ratingDTO Objeto con los datos de la valoración.
     * @return `true` si fue exitoso, `false` si hubo un error.
     */
    suspend fun rateUser(ratingDTO: RatingDTO): Boolean {
        return try {
            supabaseClient.from("ratings")
                .insert(ratingDTO)
            updateAverageRating(ratingDTO.user_id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Calcula y actualiza la media de valoraciones de un usuario en la tabla `users`.
     *
     * @param userId ID del usuario cuyo promedio se actualizará.
     * @return `true` si la actualización fue exitosa, `false` en caso de error.
     */
    private suspend fun updateAverageRating(userId: String): Boolean {
        return try {
            val ratingsResult = supabaseClient.from("ratings")
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeList<RatingDTO>()

            val avg = ratingsResult.map { it.rating }.average().toFloat()

            supabaseClient.from("users")
                .update(mapOf("rating" to avg)) {
                    filter { eq("id", userId) }
                }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Obtiene todas las valoraciones registradas.
     *
     * @return Lista de [RatingDTO] o una lista vacía si ocurre un error.
     */
    suspend fun getAllRatings(): List<RatingDTO> {
        return try {
            supabaseClient.from("ratings")
                .select()
                .decodeList<RatingDTO>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Obtiene todas las valoraciones recibidas por un usuario específico.
     *
     * @param userId ID del usuario valorado.
     * @return Lista de valoraciones o vacía si ocurre un error.
     */
    suspend fun getRatingsForRatedUser(userId: String): List<RatingDTO> {
        return try {
            supabaseClient.from("ratings")
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeList<RatingDTO>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}