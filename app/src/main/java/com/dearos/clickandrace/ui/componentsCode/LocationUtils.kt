package com.dearos.clickandrace.ui.componentsCode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dearos.clickandrace.ui.screens.appScreens.inTheZone.REQUEST_LOCATION_PERMISSION
import com.google.android.gms.location.LocationServices

/**
 * Verifica si la aplicación ya tiene permisos de ubicación otorgados por el usuario.
 *
 * @param context Contexto actual de la app (normalmente un Activity).
 * @return true si los permisos de ubicación fina y aproximada están concedidos.
 */
fun hasLocationPermissions(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fine == PackageManager.PERMISSION_GRANTED && coarse == PackageManager.PERMISSION_GRANTED
}

/**
 * Solicita al usuario los permisos necesarios para acceder a la ubicación del dispositivo.
 *
 * @param context Contexto que debe ser una instancia de ComponentActivity.
 * Lanza una petición de permisos para `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION`.
 */
fun requestLocationPermissions(context: Context) {
    ActivityCompat.requestPermissions(
        context as ComponentActivity,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        REQUEST_LOCATION_PERMISSION // Código de solicitud personalizado para identificar la respuesta
    )
}

/**
 * Obtiene la última ubicación conocida del dispositivo utilizando el FusedLocationProviderClient.
 *
 * @param context Contexto necesario para acceder al proveedor de ubicación.
 * @param onLocationReceived Callback que recibe la latitud y longitud cuando se obtiene la ubicación.
 *
 * Nota: Este método no fuerza una nueva actualización de ubicación, solo retorna la última conocida.
 */
fun getCurrentLocation(
    context: Context,
    onLocationReceived: (latitude: Double, longitude: Double) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    try {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onLocationReceived(it.latitude, it.longitude)
                }
            }
    } catch (e: SecurityException) {
        // Si los permisos no están concedidos, esta excepción puede ser lanzada
        e.printStackTrace()
    }
}
