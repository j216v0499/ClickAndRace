package com.dearos.clickandrace.auth.domain.authRepository

import com.dearos.clickandrace.LogsLogger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Encargado de actualizar la contraseña del usuario actualmente autenticado.
 *
 * Utiliza el cliente de Supabase para aplicar los cambios de contraseña al usuario actual.
 *
 * @param supabaseClient Cliente Supabase ya inicializado
 */
class SetPasswordRepo(
    private val supabaseClient: SupabaseClient
) {

    /**
     * Actualiza la contraseña del usuario autenticado actualmente.
     *
     * Esta operación solo es válida si hay un usuario con sesión activa.
     *
     * @param newPassword La nueva contraseña a establecer.
     * @return `Result.success` si se actualizó correctamente, `Result.failure` si ocurrió un error.
     */
    //El suspend se encarga de que la función no bloquee el hilo principal
    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Llama al método de Supabase para actualizar los datos del usuario (en este caso, la contraseña)
                supabaseClient.auth.updateUser {
                    password = newPassword
                }

                // Si no se lanza excepción, la operación fue correcto
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                // En caso de error, se registra en el Logger personalizado
                LogsLogger.e("SetPasswordRepository", "Error actualizando paswd: ${e.message}")
                return@withContext Result.failure(e)
            }
        }
    }
}
