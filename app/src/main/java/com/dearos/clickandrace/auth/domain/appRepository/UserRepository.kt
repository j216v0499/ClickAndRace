package com.dearos.clickandrace.auth.domain.appRepository

import android.content.Context
import android.net.Uri
import com.dearos.clickandrace.auth.domain.supabase.SupabaseClientProvider
import com.dearos.clickandrace.model.dto.UserDTO
import com.dearos.clickandrace.model.data.UserData
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Repositorio encargado de gestionar las operaciones CRUD relacionadas con los usuarios (users)
 * en la base de datos Supabase.
 *
 * Proporciona métodos para obtener y actualizar datos del usuario, obtener el ID del usuario actual
 * y cargar imágenes de perfil en Supabase Storage.
 *
 * @param supabaseClient Cliente de Supabase configurado para interactuar con la base de datos y almacenamiento.
 */
class UserRepository(private val supabaseClient: SupabaseClient) {

    /**
     * Obtiene los datos de un usuario específico a partir de su ID.
     *
     * @param id ID del usuario cuyo dato se desea obtener.
     * @return Un objeto [UserData] con la información del usuario o `null` si no se encuentra el usuario.
     */
    suspend fun getUserData(id: String): UserData? {
        return supabaseClient.from("users")
            .select {
                filter { eq("id", id) }
            }
            .decodeSingleOrNull()
    }

    /**
     * Actualiza la información de un usuario.
     *
     * @param id ID del usuario a actualizar.
     * @param user Objeto [UserDTO] que contiene los nuevos datos del usuario.
     */
    suspend fun updateUser(id: String, user: UserDTO) {
        supabaseClient.from("users")
            .update(user) {
                filter { eq("id", id) }
            }
    }

    /**
     * Obtiene el ID del usuario actual autenticado.
     *
     * @return El ID del usuario actual o `null` si no hay usuario autenticado.
     */
    fun getCurrentUserId(): String? {
        return supabaseClient.auth.currentUserOrNull()?.id
    }

    /**
     * Subir una imagen de perfil a Supabase Storage.
     *
     * @param uri Uri de la imagen que se desea cargar.
     * @param context El contexto de la aplicación necesario para acceder al contenido de la URI.
     * @return La URL pública de la imagen subida o `null` si ocurrió un error.
     */
    suspend fun uploadImageToSupabase(uri: Uri, context: Context): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@withContext null
                val fileName = "profile_pictures/${UUID.randomUUID()}.jpg"

                SupabaseClientProvider.client.storage
                    .from("avatars")
                    .upload(
                        path = fileName,
                        data = bytes,
                        options = {
                            upsert = true
                        }
                    )

                // Si el bucket es público, retornamos la URL pública
                "https://pxfipabwlotvlxxliwfj.supabase.co/storage/v1/object/public/avatars/$fileName"

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
