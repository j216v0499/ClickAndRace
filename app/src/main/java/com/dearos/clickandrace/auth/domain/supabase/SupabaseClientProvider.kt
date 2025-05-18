package com.dearos.clickandrace.auth.domain.supabase

import com.dearos.clickandrace.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

/**
 * Objeto singleton encargado de proporcionar una instancia configurada del cliente de Supabase.
 *
 * Esta clase centraliza la inicialización del cliente, facilitando su reutilización a lo largo
 * de toda la aplicación. Utiliza el patrón de inicialización perezosa (lazy) para evitar
 * múltiples instancias.
 *
 * El cliente incluye los siguientes módulos:
 * - `Auth`: Para la autenticación de usuarios, con refresco automático de tokens habilitado.
 * - `Postgrest`: Para realizar operaciones CRUD sobre la base de datos.
 * - `Storage`: Para gestión de archivos (subida, descarga, etc.).
 */
object SupabaseClientProvider {

    // Variables de configuración del proyecto (definidas en build.gradle.kts)
    private const val SUPABASE_URL = BuildConfig.SUPABASE_URL
    private const val SUPABASE_ANON_KEY = BuildConfig.SUPABASE_ANON_KEY

    /**
     * Instancia única del cliente Supabase, inicializada perezosamente.
     * Utiliza los plugins de autenticación, base de datos (PostgREST) y almacenamiento.
     */
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Auth) {
                alwaysAutoRefresh = true          // Refresca automáticamente el token de sesión
                autoLoadFromStorage = true        // Carga la sesión desde almacenamiento local si existe
            }
            install(Postgrest)                    // Acceso a la base de datos como API REST
            install(Storage)                      // Acceso a buckets y archivos
        }
    }
}
