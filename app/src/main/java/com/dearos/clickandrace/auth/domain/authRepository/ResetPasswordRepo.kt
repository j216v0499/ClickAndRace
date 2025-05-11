package com.dearos.clickandrace.auth.domain.authRepository

import com.dearos.clickandrace.LogsLogger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Encargado de gestionar las operaciones relacionadas con el restablecimiento de contraseña.
 *
 * Utiliza Supabase para:
 * - Comprobar si el correo electrónico existe en la tabla de usuarios
 * - Enviar códigos de un solo uso para hacer el restablecimiento
 *
 * @param supabaseClient Cliente de Supabase ya configurado
 */
class ResetPasswordRepo(
    private val supabaseClient: SupabaseClient
) {

    /**
     * Verifica si el email existe en la tabla de usuarios
     * y está asociado a un proveedor de autenticación tipo "email".
     *
     * @param email El email que se quiere verificar.
     * @return `true` si el email existe, `false` si no existe o hubo un error.
     */
    //El suspend se encarga de que la función no bloquee el hilo principal
    suspend fun isEmailInPublicUsersTable(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Consulta la tabla "users" en Supabase, filtrando por proveedor y email.
                val result = supabaseClient
                    .from("users")
                    .select(columns = Columns.list("email")) {
                        filter {
                            eq("provider", "email") // solo usuarios registrados con email
                            eq("email", email)
                        }
                    }
                    .decodeList<Map<String, String>>() // Convierte el resultado en lista de pares clave-valor

                result.isNotEmpty() // Devuelve true si el email existe en la tabla

            } catch (e: Exception) {
                //Todo
                LogsLogger.e(
                    "SignUpRepository",
                    "Error checking email in public.users: ${e.message}"
                )
                false
            }
        }
    }

    /**
     * Envía un código de un solo uso al email utilizando el método de Supabase.
     *
     * @param email El email del usuario que quiere restablecer su contraseña.
     * @return Resultado exitoso o con error.
     */
    suspend fun sendOTP(email: String): Result<Unit> {
        return try {
            supabaseClient.auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

