
package com.dearos.clickandrace.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LanguageLocaleHelper {

    fun getAvailableLanguages(context: Context): List<String> {
        return listOf("en", "es", "ca","fr","pt") // Puedes extender esto con más idiomas según tus archivos de strings
    }

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)


        setCurrentLanguage(context, language)


        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun getCurrentLanguage(): String {
        return Locale.getDefault().language
    }

    fun getCurrentLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("language", "en") ?: "en"
    }

    fun setCurrentLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", lang).apply()
    }

}
