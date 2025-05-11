package com.dearos.clickandrace.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.util.LanguageLocaleHelper
import java.util.Locale

import androidx.compose.ui.res.painterResource

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.layout.size

import com.dearos.clickandrace.R



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ColorFilter

import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun LanguageSelector(onLanguageSelected: (String) -> Unit) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val availableLanguages = LanguageLocaleHelper.getAvailableLanguages(context)
    val languageLabels = availableLanguages.associateWith { code ->
        Locale(code).displayName.replaceFirstChar { it.uppercase() }
    }



//
    //TODO; Ajustar las banderas de los idiomas

    val languageFlags = mapOf(
        "en" to R.drawable.ic_flag_en,
        "es" to R.drawable.ic_flag_es,
        "ca" to R.drawable.ic_flag_ca,
        "fr" to R.drawable.ic_flag_fr,
        "pt" to R.drawable.ic_flag_pt


    )

    var expanded by remember { mutableStateOf(false) }
//    var selectedLang by remember {
//        mutableStateOf(LanguageLocaleHelper.getCurrentLanguage())
//    }

    var selectedLang by remember { mutableStateOf(LanguageLocaleHelper.getCurrentLanguage(context)) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .wrapContentSize(Alignment.TopEnd)
    ) {
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

                Text(
                    text = languageLabels[selectedLang] ?: selectedLang,
                    color = if (isLoading) Color.Gray else Color.Black
                )

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

        DropdownMenu(
            expanded = expanded && !isLoading,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            availableLanguages.forEach { code ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            languageFlags[code]?.let { flagRes ->
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = flagRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
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
