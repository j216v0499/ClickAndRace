package com.dearos.clickandrace.ui.screens.appScreens.productsScreen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dearos.clickandrace.ui.componentsCode.parseWkbPoint
import com.dearos.clickandrace.ui.componentsUI.MiniMapView
import com.dearos.clickandrace.ui.screens.appScreens.ratingScreen.RatingViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    navController: NavController,
    viewModel: ProductViewModel = koinViewModel()
) {
    val ratingViewModel: RatingViewModel = koinViewModel()
    val userRatings by ratingViewModel.userRatings.collectAsState()
    var showUserRatingsDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val selectedUser by viewModel.selectedUser.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showNotAllowedDialog by remember { mutableStateOf(false) }
    var showEditNotAllowedDialog by remember { mutableStateOf(false) }
    var expandedImageUri by remember { mutableStateOf<String?>(null) }

    val hasRated by ratingViewModel.hasRated.collectAsState(initial = false) // Suponiendo que existe
    var showRatedDialog by remember { mutableStateOf(false) }

    val rotation = remember { mutableFloatStateOf(0f) }


    LaunchedEffect(productId) {
        viewModel.loadProductById(productId)
    }

    LaunchedEffect(selectedProduct, currentUserId) {
        val buyerId = currentUserId
        val sellerId = selectedProduct?.user_id

        if (buyerId != null && sellerId != null) {
            ratingViewModel.checkIfRatedLocally(buyerId, sellerId)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    selectedProduct?.let { product ->
                        IconButton(
                            onClick = {
                                if (product.user_id == currentUserId) {
                                    navController.navigate("edit_product/$productId")
                                } else {
                                    showEditNotAllowedDialog = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = if (product.user_id == currentUserId) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }

                        IconButton(
                            onClick = {
                                if (product.user_id == currentUserId) {
                                    showDeleteDialog = true
                                } else {
                                    showNotAllowedDialog = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = if (product.user_id == currentUserId) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        selectedProduct?.let { product ->

            val images = product.product_picture ?: emptyList()
            // Extraemos latitud y longitud si existe el campo geography
            val locationGeography = product.location  // Cambia esto al nombre real de tu campo WKB
            val coords = parseWkbPoint(locationGeography)

            Box(modifier = Modifier.fillMaxSize()) {

                // Scroll vertical para el contenido completo
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    // Galería de imágenes en formato 4:3
                    if (images.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(images) { url ->
                                Image(
                                    painter = rememberAsyncImagePainter(url),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillParentMaxWidth(0.85f)
                                        .aspectRatio(4f / 3f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                        .clickable {
                                            expandedImageUri = url
                                        }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Detalles del producto
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = product.title,
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Precio: ${product.price}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Tipo: ${product.type}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Descripción:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Información del vendedor con foto
                    selectedUser?.let { user ->
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Foto de perfil del vendedor
                                val profilePicUrl = user.profile_picture
                                if (!profilePicUrl.isNullOrEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(profilePicUrl),
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .border(
                                                1.5.dp,
                                                MaterialTheme.colorScheme.primary,
                                                CircleShape
                                            ).clickable {
                                                ratingViewModel.loadRatingsForUser(user.id)
                                                showUserRatingsDialog = true
                                            }
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Sin foto",
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .padding(12.dp)
                                            .clickable {
                                                ratingViewModel.loadRatingsForUser(user.id)
                                                showUserRatingsDialog = true
                                            },
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))


// Correo
                                Column(modifier = Modifier.weight(1f)) {

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            user.email?.let { email ->
                                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                    data = "mailto:$email".toUri()
                                                }
                                                ContextCompat.startActivity(context, intent, null)
                                            }
                                        }) {
                                            Icon(
                                                Icons.Default.Email,
                                                contentDescription = "Enviar correo"
                                            )
                                        }
                                        Text(
                                            text = "Correo: ${user.email}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))

// Teléfono
                                    if (!user.phone.isNullOrBlank()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                                    data = "tel:${user.phone}".toUri()
                                                }
                                                ContextCompat.startActivity(context, intent, null)
                                            }) {
                                                Icon(
                                                    Icons.Default.Phone,
                                                    contentDescription = "Llamar"
                                                )
                                            }
                                            Text(
                                                text = "Teléfono: ${user.phone}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            if (hasRated) {
                                                showRatedDialog = true
                                            } else {
                                                val sellerId = product.user_id
                                                val buyerId = currentUserId!!
                                                navController.navigate("rate_user_screen/$buyerId/$sellerId")
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !hasRated,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (hasRated) Color.Gray else MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(if (hasRated) "Ya valorado" else "Valorar al vendedor")
                                    }

                                }

                                Spacer(modifier = Modifier.width(12.dp))




                            }
                        }
                    }



                coords?.let { (latitude, longitude) ->
                    MiniMapView(
                        context = context,
                        latitude = latitude,
                        longitude = longitude,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }}

                // Diálogo de imagen ampliada con zoom y scroll
                if (expandedImageUri != null) {
                    Dialog(onDismissRequest = { expandedImageUri = null }) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.95f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val scale = remember { mutableFloatStateOf(1f) }
                            val offsetX = remember { mutableFloatStateOf(0f) }
                            val offsetY = remember { mutableFloatStateOf(0f) }
                            val rotation = remember { mutableFloatStateOf(0f) }


                            Image(
                                painter = rememberAsyncImagePainter(expandedImageUri!!),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .transformable(
                                        state = rememberTransformableState { zoomChange, panChange, rotationChange ->
                                            scale.floatValue =
                                                max(1f, min(5f, scale.floatValue * zoomChange))
                                            offsetX.floatValue += panChange.x
                                            offsetY.floatValue += panChange.y
                                            rotation.floatValue += rotationChange

                                        }
                                    )


                                    .graphicsLayer(
                                        scaleX = scale.floatValue,
                                        scaleY = scale.floatValue,
                                        rotationZ = rotation.floatValue,
                                        translationX = offsetX.floatValue,
                                        translationY = offsetY.floatValue
                                    ),
                                alignment = Alignment.Center
                            )

                            // Botón de cerrar
                            TextButton(
                                onClick = { expandedImageUri = null },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Cerrar", color = Color.White,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .padding(8.dp),
                                )
                            }

                            // Indicador de zoom (opcional)
                            if (scale.floatValue != 1f) {
                                Text(
                                    text = "Zoom: ${scale.floatValue.times(100).toInt()}%",
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(16.dp)
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .padding(8.dp),
                                    color = Color.White
                                )
                            }
                        }
                    }


                }

            }

        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteProduct(productId)
                    showDeleteDialog = false
                    navController.popBackStack()
                }) {
                    Text("Sí, eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("¿Eliminar producto?") },
            text = { Text("¿Estás seguro de que deseas eliminar este producto? Esta acción no se puede deshacer.") }
        )
    }

    if (showNotAllowedDialog) {
        AlertDialog(
            onDismissRequest = { showNotAllowedDialog = false },
            confirmButton = {
                TextButton(onClick = { showNotAllowedDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Acción no permitida") },
            text = { Text("No puedes eliminar este producto porque no eres su creador.") }
        )
    }

    if (showEditNotAllowedDialog) {
        AlertDialog(
            onDismissRequest = { showEditNotAllowedDialog = false },
            confirmButton = {
                TextButton(onClick = { showEditNotAllowedDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Acción no permitida") },
            text = { Text("No puedes editar este producto porque no eres su creador.") }
        )
    }

    if (showRatedDialog) {
        AlertDialog(
            onDismissRequest = { showRatedDialog = false },
            confirmButton = {
                TextButton(onClick = { showRatedDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Valoración ya realizada") },
            text = { Text("Ya has valorado a este vendedor anteriormente.") }
        )
    }

    if (showUserRatingsDialog) {
        AlertDialog(
            onDismissRequest = { showUserRatingsDialog = false },
            confirmButton = {
                TextButton(onClick = { showUserRatingsDialog = false }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Valoraciones del vendedor") },
            text = {
                if (userRatings.isEmpty()) {
                    Text("Este usuario aún no tiene valoraciones.")
                } else {
                    val average = userRatings.map { it.rating }.average()
                    val total = userRatings.size

                    Column {
                        // Encabezado con resumen
                        Text(
                            text = "Valoración media: ⭐ ${"%.1f".format(average)} \n ($total valoraciones)",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Scroll de valoraciones
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            items(userRatings) { rating ->
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("★ ${rating.rating}", fontWeight = FontWeight.Bold)
                                    if (rating.comment.isNotBlank()) {
                                        Text("Comentario: ${rating.comment}")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        )
    }

}
