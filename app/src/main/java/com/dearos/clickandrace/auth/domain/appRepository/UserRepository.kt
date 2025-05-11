package com.dearos.clickandrace.auth.domain.appRepository

import com.dearos.clickandrace.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val supabaseClient: SupabaseClient) {

    /** 1. Create */
    suspend fun createUser(user: User): Result<User> = withContext(Dispatchers.IO) {
        runCatching {
            val inserted: Map<String, Any> = supabaseClient
                .from("users")
                .insert(
                    mapOf(
                        "name"            to user.name,
                        "email"           to user.email,
                        "phone"           to user.phone,
                        "rating"          to user.rating,
                        "profile_picture" to user.profilePicture,
                        "default_location"     to user.defaultLocation,
                        "default_availability" to user.defaultAvailability
                    )
                )
                .decodeSingle()
            inserted.toUser()
        }
    }

    /** 2. Read by ID */
    suspend fun getUserById(id: String): Result<User> = withContext(Dispatchers.IO) {
        runCatching {
            val row: Map<String, Any> = supabaseClient
                .from("users")
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle()
            row.toUser()
        }
    }

    /** 2b. Read all (optionally paginate/filter outside) */
    suspend fun getAllUsers(): Result<List<User>> = withContext(Dispatchers.IO) {
        runCatching {
            val rows: List<Map<String, Any>> = supabaseClient
                .from("users")
                .select { /* no filter = all */ }
                .decodeList()
            rows.map { it.toUser() }
        }
    }

    /** 3. Update */
    suspend fun updateUser(id: String, updates: Map<String, Any>): Result<User> = withContext(Dispatchers.IO) {
        runCatching {
            val updated: Map<String, Any> = supabaseClient
                .from("users")
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle()
            updated.toUser()
        }
    }

    /** 4. Delete */
    suspend fun deleteUser(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val deleted: List<Map<String, Any>> = supabaseClient
                .from("users")
                .delete {
                    filter { eq("id", id) }
                }
                .decodeList()
            if (deleted.isEmpty()) throw Exception("Usuario no encontrado o ya eliminado")
            Unit
        }
    }
    /** Crea un usuario en la tabla "users" usando los datos de la sesión de autenticación */
    suspend fun createUserFromAuth(userId: String, email: String, name: String?): Result<User> {
        return createUser(
            User(
                id = userId,
                name = name ?: "Usuario sin nombre", // Asigna un nombre predeterminado si falta
                email = email,
                phone = null, // Puedes omitir o asignar valores por defecto
                rating = 0f,
                profilePicture = null,
                defaultLocation = null,
                defaultAvailability = emptyMap(),
                createdAt = null
            )
        )
    }

    /** Mapea un registro JSON a tu modelo User */
    private fun Map<String, Any?>.toUser(): User = User(
        id                  = this["id"] as String,
        name                = this["name"] as String,
        email               = this["email"] as String,
        phone               = this["phone"] as? String,
        rating              = (this["rating"] as? Number)?.toFloat() ?: 0f,
        profilePicture      = this["profile_picture"] as? String,
        defaultLocation     = this["default_location"] as? String,
        defaultAvailability = this["default_availability"] as? Map<String, Any>,
        createdAt           = this["created_at"] as? String
    )
}
