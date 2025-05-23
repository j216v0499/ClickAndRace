package com.dearos.clickandrace.ui.screens.appScreens.profileScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dearos.clickandrace.R
import com.dearos.clickandrace.ui.componentsCode.CropImageContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Pantalla composable para editar el perfil de usuario.
 *
 * Muestra campos para modificar el nombre, teléfono y foto de perfil,
 * además de mostrar correo y rating. Permite cambiar la imagen de perfil
 * mediante selección y recorte.
 *
 * @param onDismiss Callback que se ejecuta al cerrar la pantalla.
 * @param viewModel ViewModel con la lógica y datos del usuario.
 * @param coroutineScope Scope para lanzar corutinas necesarias.
 */
@Composable
fun EditUserProfileScreen(
    onDismiss: () -> Unit,
    viewModel: UserViewModel,
    coroutineScope: CoroutineScope
) {
    // Estado con los datos del usuario recogidos del ViewModel
    val userData by viewModel.userData.collectAsState()
    val context = LocalContext.current

    // Estados para los campos editables y la foto
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0.0f) }
    var profilePicUrl by remember { mutableStateOf("") }

    // Cuando cambian los datos de usuario, actualizar estados locales
    LaunchedEffect(userData) {
        userData?.let {
            name = it.name ?: ""
            email = it.email ?: ""
            phone = it.phone ?: ""
            profilePicUrl = it.profile_picture ?: ""
            rating = it.rating ?: 0.0f
        }
    }

    // Lanzador para recortar imagen con proporción 1:1
    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract(1f, 1f)
    ) { croppedUri ->
        croppedUri?.let {
            coroutineScope.launch {
                // Subir la imagen recortada y actualizar URL
                val uploadedUrl = viewModel.uploadUserProfilePicture(it, context)
                if (uploadedUrl != null) {
                    profilePicUrl = uploadedUrl
                }
            }
        }
    }

    // Lanzador para seleccionar imagen del dispositivo
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        // Cuando se selecciona una imagen, lanzar el recorte
        uri?.let { cropImageLauncher.launch(it) }
    }

    // Cargar datos del usuario actual al abrir la pantalla
    LaunchedEffect(true) {
        viewModel.loadCurrentUser()
    }

    Scaffold { padding ->
        // Contenido principal en columna scrollable con padding
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Permite scroll vertical
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Texto para título sección imagen de perfil
            Text(stringResource(id = R.string.profile_image_label), style = MaterialTheme.typography.titleMedium)

            // Mostrar imagen si la URL no está vacía
            if (profilePicUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(profilePicUrl),
                    contentDescription = stringResource(id = R.string.profile_image_content_description),
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(MaterialTheme.shapes.medium)
                )
            }

            // Botón para cambiar imagen de perfil
            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = R.string.change_image_button))
            }

            Divider()

            // Campo para editar nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.name_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo para mostrar correo (no editable)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            // Campo para editar teléfono
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(stringResource(id = R.string.phone_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para guardar cambios
            Button(
                onClick = {
                    userData?.id?.let { id ->
                        viewModel.updateUser(
                            userId = id,
                            name = name,
                            phone = phone,
                            profile_picture = profilePicUrl,
                            rating = rating
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = R.string.save_changes_button))
            }

            // Botón para cancelar edición
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = R.string.cancel_button))
            }
        }
    }
}
