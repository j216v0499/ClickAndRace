package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.R

/**
 * Diálogo que permite al usuario agregar una nueva ubicación personalizada.
 *
 * Este componente incluye campos para ingresar el nombre, latitud, longitud y tipo de la ubicación.
 * También permite seleccionar un tipo existente o introducir uno nuevo si el usuario selecciona "Otro".
 *
 * @param onDismiss Función que se ejecuta cuando se cierra el diálogo sin guardar.
 * @param onSave Función que se ejecuta al confirmar los datos ingresados (nombre, lat, lon, tipo).
 * @param currentLat Latitud actual, utilizada para prellenar el campo de latitud.
 * @param currentLon Longitud actual, utilizada para prellenar el campo de longitud.
 * @param viewModel ViewModel que contiene la lógica de los tipos de ubicación.
 */
@Composable
fun AddLocationDialog(
    onDismiss: () -> Unit,
    onSave: (String, Double, Double, String) -> Unit,
    currentLat: Double?,
    currentLon: Double?,
    viewModel: MapViewModel
) {
    // Estados locales para los campos del formulario
    var name by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf(currentLat?.toString() ?: "") }
    var lon by remember { mutableStateOf(currentLon?.toString() ?: "") }

    // Lista base de tipos de ubicación
    val baseTypes by remember { derivedStateOf { viewModel.locationTypes } }

    // Lista de tipos con la opción "Otro..." añadida
    val types = baseTypes + stringResource(id = R.string.other)
    val typesExtra = stringResource(id = R.string.other)

    // Estados para la lógica del dropdown y tipo personalizado
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var isCustomType by remember { mutableStateOf(false) }
    var customTypeInput by remember { mutableStateOf("") }

    // Al iniciarse el diálogo, se cargan los tipos únicos
    LaunchedEffect(Unit) {
        viewModel.getUniqueTypes()
    }

    // Diálogo de alerta con contenido personalizado
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.new_location)) },
        text = {
            Column {
                // Campo: Nombre de la ubicación
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.name)) }
                )

                // Campo: Latitud
                TextField(
                    value = lat,
                    onValueChange = { lat = it },
                    label = { Text(stringResource(id = R.string.label_latitude)) }
                )

                // Campo: Longitud
                TextField(
                    value = lon,
                    onValueChange = { lon = it },
                    label = { Text(stringResource(id = R.string.label_longitude)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Etiqueta para el selector de tipo
                Text(stringResource(id = R.string.label_type))

                // Selector de tipo con menú desplegable
                Box {
                    Text(
                        text = selectedType ?: stringResource(id = R.string.select_type),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(12.dp)
                    )

                    // Menú desplegable para tipos de ubicación
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
                                        // Se selecciona "Otro...", se muestra campo adicional
                                        isCustomType = true
                                        selectedType = null
                                    } else {
                                        selectedType = type
                                        isCustomType = false
                                    }
                                }
                            )
                        }
                    }
                }

                // Campo visible solo si el usuario seleccionó "Otro"
                if (isCustomType) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = customTypeInput,
                        onValueChange = { customTypeInput = it },
                        label = { Text(stringResource(id = R.string.enter_new_type)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        // Botón de confirmación (guardar)
        confirmButton = {
            Button(
                onClick = {
                    val latitude = lat.toDoubleOrNull()
                    val longitude = lon.toDoubleOrNull()
                    val finalType = if (isCustomType) customTypeInput.trim() else selectedType

                    // Validación de los campos antes de llamar a onSave
                    if (
                        name.isNotBlank() &&
                        latitude != null &&
                        longitude != null &&
                        !finalType.isNullOrBlank()
                    ) {
                        onSave(name, latitude, longitude, finalType)
                        onDismiss()
                    }
                }
            ) {
                Text(stringResource(id = R.string.button_save_changes))
            }
        },
        // Botón de cancelación
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.button_cancel_action))
            }
        }
    )
}
