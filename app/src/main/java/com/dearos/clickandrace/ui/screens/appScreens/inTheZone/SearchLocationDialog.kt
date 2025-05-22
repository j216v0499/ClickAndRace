package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dearos.clickandrace.R
import com.dearos.clickandrace.model.data.LocationData

/**
 * Cuadro de diálogo para buscar y seleccionar una ubicación.
 *
 * @param locations Lista de ubicaciones disponibles para buscar.
 * @param onDismiss Función que se ejecuta al cerrar el cuadro de diálogo.
 * @param onLocationSelected Función que se ejecuta al seleccionar una ubicación.
 */
@Composable
fun SearchLocationDialog(
    locations: List<LocationData>,
    onDismiss: () -> Unit,
    onLocationSelected: (LocationData) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(id = R.string.search_location_label)) // "Buscar ubicación"
        },
        text = {
            LocationSearchBar(
                locations = locations,
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    selectedLocation = null
                },
                onLocationSelected = {
                    selectedLocation = it
                    searchQuery = it.name
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedLocation?.let(onLocationSelected)
                },
                enabled = selectedLocation != null
            ) {
                Text(stringResource(id = R.string.dialog_search_confirm)) // "Centrar en mapa"
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.button_cancel_action)) // "Cancelar"
            }
        }
    )
}
