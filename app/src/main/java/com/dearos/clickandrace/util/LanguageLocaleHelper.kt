package com.dearos.clickandrace.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

/**
 * `LanguageLocaleHelper` es un objeto de utilidad que gestiona la configuración del idioma de la aplicación.
 *
 * Esta clase permite:
 * - Obtener la lista de idiomas disponibles.
 * - Establecer el idioma de la aplicación en tiempo de ejecución.
 * - Guardar y recuperar el idioma actual desde `SharedPreferences`.
 *
 * Está implementada como un `object`, lo cual permite su uso como singleton sin necesidad de instanciarlo.
 */
object LanguageLocaleHelper {

    /**
     * Retorna una lista de códigos de idioma disponibles.
     * Estos idiomas, definidos, en los archivos `strings.xml`,
     * en las carpetas `values-*` (por ejemplo, `values-es`, `values-fr`, etc.).
     *
     * @param context Contexto de la aplicación (no utilizado actualmente).
     * @return Lista de códigos de idioma soportados (por ejemplo: "en", "es").
     */
    fun getAvailableLanguages(context: Context): List<String> {
        return listOf("en", "es", "ca", "fr", "pt")
    }

    /**
     * Establece el idioma de la aplicación en tiempo de ejecución.
     * Cambia la configuración regional (`Locale`) y actualiza la configuración del contexto.
     * También guarda la preferencia seleccionada en `SharedPreferences`.
     *
     * @param context Contexto de la aplicación.
     * @param language Código del idioma a establecer (por ejemplo: "en", "es").
     * @return Un nuevo contexto con la configuración de idioma actualizada.
     */
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        setCurrentLanguage(context, language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // A partir de Android N se usa createConfigurationContext para aplicar la configuración.
            context.createConfigurationContext(config)
        } else {
            // Para versiones anteriores a Android N se actualiza manualmente la configuración.
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    /**
     * Recupera el idioma guardado desde `SharedPreferences`.
     * Si no existe ninguna configuración guardada, retorna "en" por defecto.
     *
     * @param context Contexto de la aplicación.
     * @return Código del idioma actual (por defecto "en").
     */
    fun getCurrentLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("language", "en") ?: "en"
    }

    /**
     * Guarda el idioma seleccionado por el usuario en `SharedPreferences`.
     *
     * @param context Contexto de la aplicación.
     * @param lang Código del idioma a guardar.
     */
    fun setCurrentLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", lang).apply()
    }
}
