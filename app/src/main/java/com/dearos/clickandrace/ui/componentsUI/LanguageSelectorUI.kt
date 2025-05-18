package com.dearos.clickandrace.ui.componentsUI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.R
import com.dearos.clickandrace.util.LanguageLocaleHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * 'LanguageSelector' muestra un selector desplegable de idiomas con banderas,
 * permitiendo que el usuario seleccione el idioma de la aplicación.
 *
 * @param onLanguageSelected Función callback que recibe el código del idioma seleccionado.
 */
@Composable
fun LanguageSelector(onLanguageSelected: (String) -> Unit) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Estado de la selección de idioma
    var expanded by remember { mutableStateOf(false) }
    val selectedLang by remember { mutableStateOf(LanguageLocaleHelper.getCurrentLanguage(context)) }

    // Obtiene la lista de idiomas disponibles y sus etiquetas
    val availableLanguages = LanguageLocaleHelper.getAvailableLanguages(context)
    val languageLabels = availableLanguages.associateWith { code ->
        Locale(code).displayName.replaceFirstChar { it.uppercase() }
    }

    // Mapa de las banderas de los idiomas
    val languageFlags = mapOf(
        "en" to R.drawable.ic_flag_en,
        "es" to R.drawable.ic_flag_es,
        "ca" to R.drawable.ic_flag_ca,
        "fr" to R.drawable.ic_flag_fr,
        "pt" to R.drawable.ic_flag_pt
    )


    // Caja principal del selector de idioma
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .wrapContentSize(Alignment.TopEnd)
    ) {
        // Botón de idioma
        OutlinedButton(
            onClick = { if (!isLoading) expanded = true },
            border = BorderStroke(1.dp, Color.Gray),
            shape = CircleShape,
            modifier = Modifier.wrapContentSize(),
            enabled = !isLoading
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Muestra la bandera del idioma seleccionado
                languageFlags[selectedLang]?.let { flagRes ->
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = flagRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = if (isLoading) ColorFilter.tint(Color.Gray) else null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Muestra el nombre del idioma seleccionado
                Text(
                    text = languageLabels[selectedLang] ?: selectedLang,
                    color = if (isLoading) Color.Gray else Color.Black
                )

                // Muestra un indicador de carga si está cargando
                if (isLoading) {
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF3A82F7)
                    )
                }
            }
        }

        // Menú desplegable con los idiomas disponibles
        DropdownMenu(
            expanded = expanded && !isLoading,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            // Itera sobre los idiomas disponibles para mostrar las opciones
            availableLanguages.forEach { code ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Muestra la bandera del idioma
                            languageFlags[code]?.let { flagRes ->
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = flagRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            // Muestra el nombre del idioma
                            Text(text = languageLabels[code] ?: code)
                        }
                    },
                    onClick = {
                        expanded = false
                        coroutineScope.launch {
                            isLoading = true
                            delay(300) // Pequeña pausa para la animación
                            onLanguageSelected(code)
                            isLoading = false
                        }
                    }
                )
            }
        }
    }
}

/**
 * Vista previa del selector de idioma para ver cómo se renderiza en el editor de Android Studio.
 */
@Preview(showBackground = true) // Permite previsualizar la pantalla en el editor de Android Studio
@Composable
fun LanguageSelectorPreview() {
    MaterialTheme {
        // Simula el comportamiento del LanguageSelector con una función vacía como callback.
        LanguageSelector(onLanguageSelected = {})
    }
}
