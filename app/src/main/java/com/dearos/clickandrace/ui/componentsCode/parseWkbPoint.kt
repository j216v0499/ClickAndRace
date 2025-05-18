package com.dearos.clickandrace.ui.componentsCode

import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Parsea una geometría en formato WKB (Well-Known Binary) codificada como hexadecimal,
 * extrayendo las coordenadas de un punto geográfico (POINT).
 *
 *
 * @param wkbHex Cadena en hexadecimal que representa una geometría WKB (por ejemplo, "0101000020E6100000...").
 * @return Un par de coordenadas (latitud, longitud) si el WKB representa un punto válido, o `null` si hay error.
 */
fun parseWkbPoint(wkbHex: String): Pair<Double, Double>? {
    return try {
        // Elimina espacios y convierte all a minúsculas para procesamiento consistente
        val cleanHex = wkbHex.replace(" ", "").lowercase()

        // Convierte el string hexadecimal en un arreglo de bytes
        val bytes = cleanHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

        // Determina el orden de bytes (endianness): 0 = Big Endian, 1 = Little Endian
        val byteOrder = when (bytes[0].toInt()) {
            0 -> ByteOrder.BIG_ENDIAN
            1 -> ByteOrder.LITTLE_ENDIAN
            else -> return null
        }

        // Prepara el buffer con el orden de bytes detectado
        val buffer = ByteBuffer.wrap(bytes)
        buffer.order(byteOrder)

        buffer.position(1) // Salta el primer byte (endianness marker)

        // Lee el tipo de geometría (y opcionalmente SRID)
        val geometryTypeWithSrid = buffer.int

        // Verifica si la geometría incluye un SRID (Spatial Reference ID)
        val hasSrid = (geometryTypeWithSrid and 0x20000000) != 0

        // Extrae solo el tipo de geometría (los 2 bytes menos significativos)
        val geometryType = geometryTypeWithSrid and 0xFFFF

        // Verifica que la geometría sea un POINT (código 1)
        if (geometryType != 1) return null

        // Si hay SRID, lo saltamos (4 bytes adicionales)
        if (hasSrid) {
            buffer.int
        }

        // Extrae las coordenadas: primero la X (longitud), luego la Y (latitud)
        val x = buffer.double
        val y = buffer.double

        // Devuelve latitud y longitud como un par
        return Pair(y, x)
    } catch (e: Exception) {
        // En caso de error (bytes malformados, tipo desconocido, etc.)
        e.printStackTrace()
        null
    }
}
