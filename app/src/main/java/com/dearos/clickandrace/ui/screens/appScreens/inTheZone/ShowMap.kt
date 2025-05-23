package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.dearos.clickandrace.R
import com.dearos.clickandrace.ui.componentsCode.getCurrentLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


/**
 * Composable que muestra un mapa usando Osmdroid dentro de Compose.
 * Muestra ubicaciones obtenidas desde el ViewModel con marcadores personalizados.
 */
@Composable
fun ShowMap(context: Context, viewModel: MapViewModel) {
    val coroutineScope = rememberCoroutineScope() // Para lanzar corutinas dentro del Composable
    var mapView by remember { mutableStateOf<MapView?>(null) } // Referencia al MapView nativo
    var currentLocation by remember { mutableStateOf<GeoPoint?>(null) } // Ubicación actual del usuario

    // Efecto que se lanza una vez para cargar ubicaciones y obtener ubicación actual
    LaunchedEffect(Unit) {
        // Cargar ubicaciones desde ViewModel en un hilo IO
        coroutineScope.launch(Dispatchers.IO) {
            viewModel.getLocations()
        }

        // Obtener ubicación actual y centrar mapa en ella
        getCurrentLocation(context) { lat, lon ->
            currentLocation = GeoPoint(lat, lon)

            // Si el mapa ya está inicializado, centrar y hacer zoom en la ubicación actual
            mapView?.controller?.setCenter(currentLocation)
            mapView?.controller?.setZoom(15.0)

            // Refrescar el mapa para mostrar cambios
            mapView?.invalidate()
        }
    }

    // Componente AndroidView para integrar MapView (vista nativa) dentro de Compose
    AndroidView(
        factory = {
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK) // Estilo de mapa
                controller.setZoom(10.0) // Zoom inicial
                controller.setCenter(currentLocation ?: GeoPoint(40.4168, -3.7038)) // Centro Madrid por defecto
                mapView = this // Guardar referencia para uso posterior
            }
        },
        modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio disponible
        update = { map ->
            // Primero eliminar marcadores anteriores para evitar duplicados
            map.overlays.filterIsInstance<Marker>().forEach { map.overlays.remove(it) }

            // Agregar un marcador por cada ubicación en el ViewModel
            viewModel.locations.forEach { location ->
                val marker = Marker(map).apply {
                    position = GeoPoint(location.latitude, location.longitude)
                    title = location.name
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    // Selección del icono según el tipo de ubicación
                    val type = location.type?.lowercase() ?: ""
                    icon = when {
                        type.contains("circuito") -> ContextCompat.getDrawable(context, R.drawable.ic_circuito)
                        type.contains("taller") -> ContextCompat.getDrawable(context, R.drawable.ic_taller)
                        type.contains("parque") -> ContextCompat.getDrawable(context, R.drawable.ic_parque)
                        type.contains("parquing") -> ContextCompat.getDrawable(context, R.drawable.ic_parquing)
                        type.contains("supermercado") -> ContextCompat.getDrawable(context, R.drawable.ic_supermercado)
                        type.contains("recambios") -> ContextCompat.getDrawable(context, R.drawable.ic_recambios)
                        type.contains("desguace") -> ContextCompat.getDrawable(context, R.drawable.ic_desguace)
                        else -> ContextCompat.getDrawable(context, R.drawable.ic_default_marker) // Icono por defecto
                    }
                }
                map.overlays.add(marker) // Añadir marcador al mapa
            }

            // Si hay una ubicación seleccionada para enfocar, centrar y hacer zoom en ella
            viewModel.selectedFocus?.let { focus ->
                val point = GeoPoint(focus.latitude, focus.longitude)
                map.controller.setCenter(point)
                map.controller.setZoom(17.0)
                viewModel.focusOnLocation(null) // Limpiar foco después de centrar
            }

            map.invalidate() // Refrescar mapa para que se muestren todos los cambios
        }
    )
}
