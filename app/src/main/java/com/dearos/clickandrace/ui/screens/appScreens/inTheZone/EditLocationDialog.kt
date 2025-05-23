package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * Diálogo para editar una ubicación existente.
 *
 * Permite seleccionar una ubicación, modificar sus datos y actualizarla.
 */
@Composable
fun EditLocationDialog(
    locations: List<LocationData>,
    onDismiss: () -> Unit,
    viewModel: MapViewModel,
    coroutineScope: CoroutineScope,
) {
    // Estados para los campos editables
    var newName by remember { mutableStateOf("") }
    var newLat by remember { mutableStateOf("") }
    var newLon by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf("") }

    // Tipos disponibles para seleccionar o añadir uno nuevo
    val baseTypes by remember { derivedStateOf { viewModel.locationTypes } }
    val types = baseTypes + stringResource(id = R.string.other)
    val typesExtra = stringResource(id = R.string.other)

    // Estados de UI relacionados con selección y entrada
    var expanded by remember { mutableStateOf(false) }
    var isCustomType by remember { mutableStateOf(false) }
    var customTypeInput by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<String?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }


    val userId by viewModel.currentUserId.collectAsState()

    // Precargar los campos cuando se selecciona una ubicación
    LaunchedEffect(selectedLocation) {
        selectedLocation?.let {
            newName = it.name
            newLat = it.latitude.toString()
            newLon = it.longitude.toString()
            newType = it.type ?: ""
            selectedType = it.type
            isCustomType = false
            customTypeInput = ""
        }
    }

    // Obtener tipos únicos al abrir el diálogo
    LaunchedEffect(Unit) {
        viewModel.getUniqueTypes()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.edit_location)) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                // Si no hay ubicación seleccionada, mostrar el buscador
                if (selectedLocation == null) {
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
                        userId = userId // <-- Este valor viene de viewModel.currentUserId.collectAsState().value

                    )
                } else {
                    // Campos editables
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text(stringResource(id = R.string.name)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newLat,
                        onValueChange = { newLat = it },
                        label = { Text(stringResource(id = R.string.label_latitude)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newLon,
                        onValueChange = { newLon = it },
                        label = { Text(stringResource(id = R.string.label_longitude)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector de tipo
                    Text(stringResource(id = R.string.label_type))
                    Box {
                        Text(
                            text = if (isCustomType) {
                                customTypeInput.ifBlank { stringResource(id = R.string.enter_new_type) }
                            } else {
                                selectedType ?: stringResource(id = R.string.select_type)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = true }
                                .padding(12.dp)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            types.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        expanded = false
                                        if (type == typesExtra) {
                                            isCustomType = true
                                            selectedType = null
                                            customTypeInput = ""
                                        } else {
                                            isCustomType = false
                                            selectedType = type
                                            customTypeInput = ""
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Campo de tipo personalizado
                    if (isCustomType) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customTypeInput,
                            onValueChange = { customTypeInput = it },
                            label = { Text(stringResource(id = R.string.enter_new_type)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalType = if (isCustomType) customTypeInput.trim() else selectedType

                    if (
                        selectedLocation != null &&
                        newName.isNotBlank() &&
                        newLat.toDoubleOrNull() != null &&
                        newLon.toDoubleOrNull() != null &&
                        !finalType.isNullOrBlank()
                    ) {
                        coroutineScope.launch {

                                userId?.let {
                                    viewModel.updateLocation(

                                        selectedLocation!!.id,
                                        it,
                                        newName,
                                        newLat.toDouble(),
                                        newLon.toDouble(),
                                        finalType
                                    )
                                }

                            onDismiss()
                        }
                    }
                },
                enabled = selectedLocation != null
            ) {
                Text(stringResource(id = R.string.button_save_changes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.button_cancel_action))
            }
        }
    )
}
