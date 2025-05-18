package com.dearos.clickandrace.ui.componentsCode

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.yalantis.ucrop.UCrop
import java.io.File

/**
 * Se utiliza la librería UCrop para recortar imágenes.
 * Permite iniciar una actividad de recorte con un aspecto determinado y dimensiones máximas.
 *
 * @param aspectRatioX Relación de aspecto horizontal (por defecto 1f para cuadrado).
 * @param aspectRatioY Relación de aspecto vertical (por defecto 1f para cuadrado).
 * @param maxWidth Ancho máximo del resultado recortado (por defecto 800 px).
 * @param maxHeight Alto máximo del resultado recortado (por defecto 800 px).
 */
class CropImageContract(
    private val aspectRatioX: Float = 1f,
    private val aspectRatioY: Float = 1f,
    private val maxWidth: Int = 800,
    private val maxHeight: Int = 800
) : ActivityResultContract<Uri, Uri?>() {

    /**
     * Crea el intent para iniciar la actividad de recorte.
     * UCrop se configura con los parámetros establecidos: aspecto, tamaño máximo, etc.
     *
     * @param context Contexto usado para generar el intent.
     * @param input Uri de la imagen original que se desea recortar.
     * @return Intent configurado para UCrop.
     */
    override fun createIntent(context: Context, input: Uri): Intent {
        // Se define la URI del archivo temporal de salida
        val destinationUri = Uri.fromFile(File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))

        // Configuración de UCrop: relación de aspecto y tamaño máximo
        return UCrop.of(input, destinationUri)
            .withAspectRatio(aspectRatioX, aspectRatioY)
            .withMaxResultSize(maxWidth, maxHeight)
            .getIntent(context)
    }

    /**
     * Procesa el resultado devuelto por UCrop.
     * Si el recorte fue exitoso, devuelve la URI del archivo recortado.
     *
     * @param resultCode Código de resultado de la actividad (por ejemplo, RESULT_OK).
     * @param intent Intent devuelto por UCrop con los datos de salida.
     * @return Uri de la imagen recortada o null si falló o se canceló.
     */
    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            UCrop.getOutput(intent)
        } else null
    }
}
