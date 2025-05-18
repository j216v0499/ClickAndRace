package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.R
import com.dearos.clickandrace.model.data.LocationData

/**
 * Barra de búsqueda de ubicaciones con resultados desplegables.
 *
 * @param locations Lista de ubicaciones disponibles.
 * @param searchQuery Texto actual del campo de búsqueda.
 * @param onSearchQueryChange Callback cuando el texto cambia.
 * @param onLocationSelected Callback cuando se selecciona una ubicación.
 * @param modifier Modificador opcional para la composición.
 */
@Composable
fun LocationSearchBar(
    locations: List<LocationData>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onLocationSelected: (LocationData) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Filtrar ubicaciones por coincidencia de nombre
    val filteredLocations = locations.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }.sortedBy { it.name.lowercase() }

    Column(modifier = modifier) {
        // Campo de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text(stringResource(id = R.string.search_location_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(id = R.string.search_placeholder)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de resultados filtrados
        Column(
            modifier = Modifier
                .heightIn(max = 200.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            filteredLocations.forEach { location ->
                Text(
                    text = location.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLocationSelected(location) }
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                )
            }
        }
    }
}
