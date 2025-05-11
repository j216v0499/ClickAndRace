package com.dearos.clickandrace.auth.domain.authRepository

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.dearos.clickandrace.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

                                    //build.gradle.kts :app
private const val WEB_CLIENT_ID =   BuildConfig.WEB_CLIENT_ID
/**
 * Encargado de gestionar la autenticación del usuarios con
 * Supabase, inicio de sesión con email, Google y fin de sesión
 *
 * @property context Contexto de la aplicación o actividad
 * @property supabaseClient Cliente de Supabase ya configurado
 */
class LoginRepo(
    private val context: Context,
    val supabaseClient: SupabaseClient
) {
    //El suspend se encarga de que la función no bloquee el hilo principal
    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Iniciar sesión con Supabase usando el Email
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                // Obtener la sesión y el usuario autenticado
                val session = supabaseClient.auth.currentSessionOrNull()
                val user = supabaseClient.auth.retrieveUserForCurrentSession()

                if (user != null && session != null) {
                    // Si el usuario y la sesión son válidos,Supabase se encarga de guardarlos.
                    Result.success(Unit)
                } else {
                    //TODO; Hacer multilingue
                    Result.failure(Exception("Credenciales inválidas. Intenta de nuevo."))
                }
            } catch (e: Exception) {
                // Verifica si el email no está confirmado
                if (e.message?.contains("Email not confirmed") == true) {
                    supabaseClient.auth.resendEmail(OtpType.Email.SIGNUP, email)
                    //TODO; Hacer multilingue
                    Result.failure(Exception("Correo no verificado. Se ha enviado un email de verificación."))
                } else {
                    Result.failure(e)
                }
            }
        }
    }

    /**
     * Inicia sesión con Google  CredentialManager y Supabase.
     *
     * @param activity Actividad desde la cual se lanza el flujo de autenticación
     * @return Result<Unit> con éxito o fallo según el resultado
     */
    //El suspend se encarga de que la función no bloquee el hilo principal
    suspend fun googleSignIn(activity: Activity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Instancia de CredentialManager
                val credentialManager = CredentialManager.create(activity)

                // Genera un nonce aleatorio y lo hashea con SHA-256
                val rawNonce = UUID.randomUUID().toString()
                val md = MessageDigest.getInstance("SHA-256")
                val hashedNonce = md.digest(rawNonce.toByteArray())
                    .joinToString("") { "%02x".format(it) }

                // Crea la opción de login con Google
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID) // ¡REEMPLAZAR!
                    .setNonce(hashedNonce)
                    .build()

                // Construye la solicitud de credencial
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // Solicita las credenciales al sistema
                val result = credentialManager.getCredential(request = request, context = activity)

                // Extrae el token de ID para Google del resultado
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                // Inicia sesión en Supabase usando el ID token y el nonce original
                supabaseClient.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                val session = supabaseClient.auth.currentSessionOrNull()
                if (session != null) {
                    Result.success(Unit)
                } else {
                    //TODO; Hacer multilingue
                    Result.failure(Exception("Error al iniciar sesión con Google: No se devolvió la sesión."))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Cierra la sesión del usuario actual en Supabase.
     *
     * @return Result<Unit> con éxito o fallo según el resultado
     */
    suspend fun logoutUser(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseClient.auth.signOut()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}