package com.dearos.clickandrace.auth.domain.authRepository

import com.dearos.clickandrace.LogsLogger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Repositorio encargado del proceso de registro y verificación de usuarios en Supabase.
 *
 * Ofrece funciones para registrar un nuevo usuario, verificar su email con OTP
 * y comprobar si un correo ya está registrado en la base de datos.
 *
 * @param supabaseClient Cliente de Supabase para ejecutar operaciones de autenticación y base de datos.
 */
class SignUpRepo(
    private val supabaseClient: SupabaseClient
) {

    /**
     * Registra un nuevo usuario con email y contraseña, y guarda su nombre completo.
     *
     * Se utiliza el proveedor 'Email' de Supabase y se añade el campo `full_name` como metadata.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña para la cuenta.
     * @param fullName Nombre completo del usuario.
     * @return Resultado de la operación: `Result.success(Unit)` si fue exitoso, `Result.failure(e)` si ocurrió un error.
     */
    //El suspend se encarga de que la función no bloquee el hilo principal

    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Registro usando el proveedor de correo electrónico
                supabaseClient.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    // Datos adicionales que se almacenan junto con el usuario
                    data = buildJsonObject {
                        put("full_name", fullName)
                    }
                }
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    /**
     * Verifica el email del usuario mediante el código OTP enviado por Supabase.
     *
     * Esta verificación es necesaria si el correo aún no ha sido confirmado tras el registro.
     *
     * @param email Correo electrónico del usuario.
     * @param token Código OTP que el usuario recibió por correo.
     * @return Resultado de la verificación.
     */
    //El suspend se encarga de que la función no bloquee el hilo principal

    suspend fun verifyEmail(email: String, token: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Verificación mediante token OTP enviado por correo
                supabaseClient.auth.verifyEmailOtp(
                    type = OtpType.Email.EMAIL,
                    email = email,
                    token = token
                )
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    /**
     * Verifica si un correo electrónico ya existe en la tabla pública `users` de Supabase.
     *
     * Útil para prevenir registros duplicados y validar formularios de registro.
     *
     * @param email Email a verificar.
     * @return `true` si el email ya está registrado, `false` en caso contrario.
     */
    suspend fun isEmailInPublicUsersTable(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Se consulta la tabla 'users' y se filtra por email
                val result = supabaseClient
                    .from("users")
                    .select(columns = Columns.list("email")) {
                        filter {
                            eq("email", email)
                        }
                    }
                    .decodeList<Map<String, String>>() // Decodifica a lista de emails
                result.isNotEmpty()
            } catch (e: Exception) {
                //Todo
                LogsLogger.e("SignUpRepository", "Error checking email in public.users: ${e.message}")
                false
            }
        }
    }
}
