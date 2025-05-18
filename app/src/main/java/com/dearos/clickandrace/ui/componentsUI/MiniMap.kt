package com.dearos.clickandrace.ui.componentsUI

import android.content.Context
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * `MiniMapView` es una vista de mapa pequeña que muestra una ubicación específica usando la librería osmdroid.
 *
 * @param context El contexto de la aplicación necesario para configurar osmdroid.
 * @param latitude Latitud de la ubicación a mostrar en el mapa.
 * @param longitude Longitud de la ubicación a mostrar en el mapa.
 * @param modifier Modificador para personalizar el diseño de la vista (opcional).
 */
@Composable
fun MiniMapView(
    context: Context,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {

    // Carga la configuración de osmdroid desde las preferencias compartidas.
    val sharedPreferences = context.getSharedPreferences("osmdroid_config", Context.MODE_PRIVATE)
    Configuration.getInstance().load(context, sharedPreferences)

    // Usamos AndroidView para integrar MapView de osmdroid dentro de una interfaz de Compose
    AndroidView(
        factory = {
            MapView(it).apply {
                // Establece el tipo de mapa (en este caso, un mapa base de Mapnik)
                setTileSource(TileSourceFactory.MAPNIK)

                // Habilita el soporte de multi-toque para el mapa
                setMultiTouchControls(true)

                // Establece el zoom inicial y la ubicación centrada en el mapa
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(latitude, longitude))

                // Crea un marcador en la ubicación proporcionada
                val marker = Marker(this)
                marker.position = GeoPoint(latitude, longitude)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Ubicación del usuario"
                overlayManager.add(marker) // Añade el marcador al mapa
            }
        },
        modifier = modifier
            .size(150.dp) // Establece el tamaño de la mini-mapa (150x150 dp)
            .clip(RoundedCornerShape(8.dp)) // Aplica un borde redondeado al mapa
    )
}

/**
 * Vista previa para ver cómo se renderiza la vista de mapa en el editor de Android Studio.
 * Usamos un ejemplo de coordenadas de Madrid (40.416775, -3.703790).
 */
@Preview(showBackground = true) // Permite previsualizar la pantalla en el editor de Android Studio
@Composable
fun MiniMapViewScreenPreview() {
    // Previsualiza el MiniMapView usando el contexto actual de la aplicación y unas coordenadas de ejemplo
    MaterialTheme {
        MiniMapView(context = LocalContext.current, latitude = 40.416775, longitude = -3.703790)
    }
}
