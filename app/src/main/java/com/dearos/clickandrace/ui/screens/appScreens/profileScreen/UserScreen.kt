package com.dearos.clickandrace.ui.screens.appScreens.profileScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dearos.clickandrace.R
import com.dearos.clickandrace.ui.screens.appScreens.ratingScreen.RatingViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla composable que muestra el perfil del usuario actual.
 *
 * Permite ver información personal, reseñas recibidas,
 * cerrar sesión y acceder a la edición del perfil.
 *
 * @param onLogout Callback que se ejecuta cuando el usuario cierra sesión.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    onLogout: () -> Unit
) {
    val userViewModel: UserViewModel = koinViewModel()
    val ratingViewModel: RatingViewModel = koinViewModel()
    val coroutineScope = rememberCoroutineScope()

    // Estados observados de usuario y sus reseñas
    val userData by userViewModel.userData.collectAsState()
    val userRatings by ratingViewModel.userRatings.collectAsState()

    var showEditScreen by remember { mutableStateOf(false) }

    // Cargar datos de usuario actual al abrir pantalla
    LaunchedEffect(true) {
        userViewModel.loadCurrentUser()
    }

    userData?.let { user ->
        // Cargar reseñas del usuario cuando cambia el ID
        LaunchedEffect(user.id) {
            ratingViewModel.loadRatingsForUser(user.id)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.user_profile_title)) },
                    actions = {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    userViewModel.logout {
                                        onLogout()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = stringResource(id = R.string.logout_content_description),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = R.string.logout_button),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Mostrar imagen de perfil si existe URL
                user.profile_picture?.let { imageUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(id = R.string.profile_image_content_description),
                        modifier = Modifier
                            .height(160.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                // Información básica del usuario
                Text(
                    text = stringResource(id = R.string.user_name_label, user.name),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.user_email_label, user.email),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(
                        id = R.string.user_phone_label,
                        user.phone ?: stringResource(id = R.string.no_phone_text)
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )

                Divider()

                // Título de reseñas recibidas
                Text(
                    text = stringResource(id = R.string.received_reviews_title),
                    style = MaterialTheme.typography.titleMedium
                )

                if (userRatings.isEmpty()) {
                    Text(text = stringResource(id = R.string.no_reviews_yet))
                } else {
                    userRatings.forEach { rating ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.Yellow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${rating.rating}/5",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = rating.comment ?: stringResource(id = R.string.no_comment_text),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para editar perfil
                Button(
                    onClick = { showEditScreen = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.edit_profile_button))
                }
            }

            // Mostrar pantalla para editar perfil cuando se activa
            if (showEditScreen) {
                EditUserProfileScreen(
                    onDismiss = { showEditScreen = false },
                    viewModel = userViewModel,
                    coroutineScope = coroutineScope
                )
            }
        }
    } ?: run {
        // Mostrar indicador de carga mientras no hay datos de usuario
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
