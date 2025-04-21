package com.dearos.clickandrace

import android.util.Log

/**
 * Logger es una clase para centralizar logs en la aplicación.
 * Permite imprimir mensajes de log en diferentes niveles de gravedad.
 *
 * Esta clase es un patrón singleton.
 *
 * Utiliza un **BASE_TAG** común para todos los mensajes de log,
 * lo que facilita la identificación  y  mejor organización.
 *
 * @param tag El origen del log.
 * @param message El mensaje que se para imprimir en el logcat.
 *
 */
object LogsLogger {
    /** El prefijo que se añadirá a todos los logs */
    private const val BASE_TAG_LOGGER =  BuildConfig.BASE_TAG_LOGGER

    /**
     * Mensaje de tipo **Error**.
     *
     */
    fun e(tag: String, message: String) {
        Log.e("$BASE_TAG_LOGGER-$tag", message)
    }

    /**
     * Mensaje de tipo **Debug**.
     *
     */
    fun d(tag: String, message: String) {
        Log.d("$BASE_TAG_LOGGER-$tag", message)
    }

    /**
     * Mensaje de tipo **Información**.
     *
     */
    fun i(tag: String, message: String) {
        Log.i("$BASE_TAG_LOGGER-$tag", message)
    }
}
