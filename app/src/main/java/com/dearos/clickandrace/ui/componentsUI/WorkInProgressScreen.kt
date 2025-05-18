package com.dearos.clickandrace.ui.componentsUI

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.R

/**
 * `WorkInProgressScreen` es una pantalla que muestra un mensaje de trabajo en progreso.
 * Esta pantalla puede utilizarse como marcador de posición mientras una funcionalidad
 * está siendo desarrollada o cargada. Incluye una animación de escalado para el ícono
 * y un mensaje que informa al usuario que la funcionalidad está en progreso.
 */
@Composable
fun WorkInProgressScreen() {
    // Creación de una animación infinita de transición para el ícono
    val infiniteTransition = rememberInfiniteTransition(label = stringResource(id = R.string.content_description_icon))

    // Definición de la animación de escalado: el ícono crecerá y disminuirá infinitamente
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, // Valor inicial de la escala
        targetValue = 2.1f, // Valor máximo de la escala
        animationSpec = infiniteRepeatable( // Especificación de la animación repetida
            animation = tween(10000, easing = FastOutLinearInEasing), // Duración y tipo de easing
            repeatMode = RepeatMode.Reverse // Hace que la animación invierta su dirección al repetirse
        ),
        label = stringResource(id = R.string.content_description_icon) // Etiqueta para la animación (para accesibilidad)
    )

    // Caja que contiene all the el contenido de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize() // La caja ocupa toda la pantalla
            .padding(24.dp), // Añadimos un padding para no pegar los elementos a los bordes
        contentAlignment = Alignment.Center // Alinea el contenido al centro
    ) {
        // Columna para organizar los elementos verticalmente
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Ícono animado (el ícono de la app) que se agranda y disminuye de tamaño
            Icon(
                painter = painterResource(id = R.drawable.clickandrace_app), // Ícono de la app
                contentDescription = stringResource(id = R.string.content_description_icon), // Descripción accesible del ícono
                modifier = Modifier
                    .size(120.dp) // Establece el tamaño inicial del ícono
                    .scale(scale), // Aplica el efecto de escala de la animación
                tint = MaterialTheme.colorScheme.primary // Color del ícono según el tema
            )

            // Espaciado entre el ícono y el siguiente texto
            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje principal de "Trabajo en progreso"
            Text(
                text = stringResource(id = R.string.work_in_progress_message),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold // Hace que el texto sea en negrita
                ),
                color = MaterialTheme.colorScheme.onBackground, // Color del texto según el tema
                textAlign = TextAlign.Center // Alinea el texto al centro
            )

            // Espaciado entre el mensaje principal y el detalle
            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje detallado informando al usuario sobre el trabajo en progreso
            Text(
                text = stringResource(id = R.string.work_in_progress_detail),
                style = MaterialTheme.typography.bodyMedium, // Estilo para texto de cuerpo
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), // Color del texto con opacidad
                textAlign = TextAlign.Center // Alinea el texto al centro
            )
        }
    }
}

@Preview(showBackground = true) // Permite previsualizar la pantalla en el editor de Android Studio
@Composable
fun WorkInProgressScreenPreview() {
    // Previsualiza la pantalla de "Trabajo en progreso" usando el MaterialTheme
    MaterialTheme {
        WorkInProgressScreen()
    }
}
