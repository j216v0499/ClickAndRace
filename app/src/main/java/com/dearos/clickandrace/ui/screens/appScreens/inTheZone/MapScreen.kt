package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.ui.componentsCode.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.osmdroid.config.Configuration


const val REQUEST_LOCATION_PERMISSION = 100

/**
 * Actividad principal que lanza la pantalla de mapa.
 */
class MapScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapScreen()
        }
    }
}

/**
 * Pantalla de mapa con funcionalidades de agregar, editar, eliminar y buscar ubicaciones.
 * Utiliza osmdroid para renderizar el mapa.
 */
@Composable
fun MapScreen(viewModel: MapViewModel = koinViewModel()) {
    val context = LocalContext.current

    // Configura osmdroid con preferencias compartidas para evitar problemas de acceso a red
    val sharedPreferences = context.getSharedPreferences("osmdroid_config", Context.MODE_PRIVATE)
    Configuration.getInstance().load(context, sharedPreferences)

    val coroutineScope = rememberCoroutineScope()

    // Estados para controlar visibilidad de los diálogos
    var showNewDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }

    // Lista de ubicaciones observadas desde el ViewModel
    val locations = viewModel.locations

    // Estados para la ubicación actual del usuario
    var currentLat by remember { mutableStateOf<Double?>(null) }
    var currentLon by remember { mutableStateOf<Double?>(null) }

    // Lanzar efecto una sola vez al cargar el Composable
    LaunchedEffect(true) {
        // Obtener ubicaciones desde la base de datos (Supabase)
        coroutineScope.launch(Dispatchers.IO) {
            viewModel.getLocations()
        }

        // Obtener la ubicación actual del dispositivo
        getCurrentLocation(context) { lat, lon ->
            currentLat = lat
            currentLon = lon
        }
    }

    // Verificar permisos de ubicación antes de renderizar el mapa
    if (hasLocationPermissions(context)) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Renderiza el mapa con las ubicaciones
            ShowMap(context, viewModel)

            // Botones flotantes en la esquina inferior derecha
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                // Botón: Añadir ubicación
                FloatingActionButton(
                    onClick = { showNewDialog = true },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir ubicación")
                }

                // Botón: Editar ubicación existente
                FloatingActionButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar ubicación")
                }

                // Botón: Eliminar ubicación
                FloatingActionButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar ubicación")
                }

                // Botón: Buscar ubicación en lista
                FloatingActionButton(
                    onClick = { showSearchDialog = true },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar ubicación")
                }
            }

            // Diálogo: Añadir nueva ubicación
            if (showNewDialog) {
                AddLocationDialog(
                    onDismiss = { showNewDialog = false },
                    onSave = { name, lat, lon, type ->
                        coroutineScope.launch {
                            viewModel.addLocation(name, lat, lon, type)
                        }
                        showNewDialog = false
                    },
                    currentLat = currentLat,
                    currentLon = currentLon,
                    viewModel = viewModel
                )
            }

            // Diálogo: Editar ubicación existente
            if (showEditDialog) {
                EditLocationDialog(
                    locations = locations,
                    onDismiss = { showEditDialog = false },
                    viewModel = viewModel,
                    coroutineScope = coroutineScope
                )
            }

            // Diálogo: Eliminar ubicación
            if (showDeleteDialog) {
                DeleteLocationDialog(
                    locations = locations,
                    onDismiss = { showDeleteDialog = false },
                    viewModel = viewModel,
                    coroutineScope = coroutineScope
                )
            }

            // Diálogo: Buscar y enfocar ubicación
            if (showSearchDialog) {
                SearchLocationDialog(
                    locations = locations,
                    onDismiss = { showSearchDialog = false },
                    onLocationSelected = { location ->
                        coroutineScope.launch {
                            viewModel.focusOnLocation(location)
                        }
                        showSearchDialog = false
                    }
                )
            }
        }
    } else {
        // Si no hay permisos, solicitarlos
        requestLocationPermissions(context)
    }
}
