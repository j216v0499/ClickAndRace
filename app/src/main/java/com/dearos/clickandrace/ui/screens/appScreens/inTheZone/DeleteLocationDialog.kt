package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.R
import com.dearos.clickandrace.model.data.LocationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Diálogo para eliminar una ubicación seleccionada de una lista.
 *
 * Permite buscar ubicaciones por nombre, seleccionarlas y confirmar su eliminación.
 *
 * @param locations Lista de ubicaciones disponibles para eliminar.
 * @param onDismiss Función a ejecutar al cerrar el diálogo.
 * @param viewModel ViewModel que gestiona las operaciones del mapa.
 * @param coroutineScope Alcance de corrutina para realizar operaciones asincrónicas.
 */
@Composable
fun DeleteLocationDialog(
    locations: List<LocationData>,
    onDismiss: () -> Unit,
    viewModel: MapViewModel,
    coroutineScope: CoroutineScope
) {
    // Estado para el texto de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Estado para la ubicación seleccionada
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }

    val userId by viewModel.currentUserId.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,

        // Título del diálogo
        title = { Text(stringResource(id = R.string.delete_location_title)) },

        // Contenido del diálogo
        text = {
            Column {
                // Componente reutilizable para buscar ubicaciones
                LocationSearchBar(
                    locations = locations,
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                        selectedLocation = null // Limpiar selección al cambiar texto
                    },
                    onLocationSelected = {
                        selectedLocation = it
                        searchQuery = it.name
                    },
                    userId = userId // <-- Este valor viene de viewModel.currentUserId.collectAsState().value


                )
                Spacer(modifier = Modifier.height(8.dp))

                // Si se ha seleccionado una ubicación, mostrar mensaje de confirmación
                selectedLocation?.let {
                    Text(
                        text = stringResource(id = R.string.delete_location_confirmation, it.name),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },

        // Botón de confirmación (Eliminar)
        confirmButton = {
            Button(
                onClick = {
                    selectedLocation?.let {
                        coroutineScope.launch {
                            viewModel.deleteLocation(it.id) // Eliminar desde el ViewModel
                            onDismiss()
                        }
                    }
                },
                enabled = selectedLocation != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error // Botón rojo
                )
            ) {
                Text(
                    text = stringResource(id = R.string.button_confirm),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        },

        // Botón de cancelar
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.button_cancel_action))
            }
        }
    )
}
