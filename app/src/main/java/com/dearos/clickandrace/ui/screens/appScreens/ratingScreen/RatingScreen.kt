package com.dearos.clickandrace.ui.screens.appScreens.ratingScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dearos.clickandrace.model.dto.RatingDTO
import org.koin.androidx.compose.koinViewModel

import androidx.compose.ui.res.stringResource
import com.dearos.clickandrace.R

/**
 * Pantalla para permitir que un usuario califique a otro.
 *
 * @param raterId ID del usuario que realiza la calificación.
 * @param ratedUserId ID del usuario que está siendo calificado.
 * @param navController Controlador de navegación para regresar al stack anterior.
 * @param viewModel ViewModel de la pantalla, inyectado por Koin por defecto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(
    raterId: String,
    ratedUserId: String,
    navController: NavController,
    viewModel: RatingViewModel = koinViewModel()
) {
    // Estados para la calificación y el comentario
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    // Observa los estados del ViewModel
    val operationSuccess by viewModel.operationSuccess.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Vuelve atrás al completar la operación
    LaunchedEffect(operationSuccess) {
        if (operationSuccess) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.rating_screen_title))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Título de la pantalla
                Text(
                    text = stringResource(id = R.string.rating_screen_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                // Fila con estrellas de calificación
                Row {
                    (1..5).forEach {
                        IconButton(onClick = { rating = it }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (it <= rating) Color.Yellow else Color.Gray
                            )
                        }
                    }
                }

                // Campo de texto para el comentario
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text(stringResource(id = R.string.comment_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón para enviar la calificación
                Button(
                    onClick = {
                        viewModel.rateUser(
                            RatingDTO(
                                rater_id = raterId,
                                user_id = ratedUserId,
                                rating = rating,
                                comment = comment
                            )
                        )
                    },
                    enabled = !isLoading && rating > 0 && comment.isNotBlank(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator() // Mostrar loading si está cargando
                    } else {
                        Text(stringResource(id = R.string.send_button))
                    }
                }
            }
        }
    )
}
