package com.dearos.clickandrace.ui.screens.appScreens.productsScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dearos.clickandrace.R
import com.dearos.clickandrace.model.dto.ProductDTO
import com.dearos.clickandrace.ui.componentsCode.CropImageContract
import com.dearos.clickandrace.ui.componentsCode.getCurrentLocation
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

/**
 * Pantalla para crear o editar un producto.
 *
 * @param productId Id del producto a editar, si es null o vacío se crea uno nuevo.
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @param viewModel ViewModel que maneja la lógica de la pantalla.
 */
@Composable
fun CreateEditProductScreen(
    productId: String? = null,
    navController: NavController,
    viewModel: ProductViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val product by viewModel.selectedProduct.collectAsState()

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Listas para imágenes nuevas y URLs de imágenes eliminadas
    val croppedImageUris = remember { mutableStateListOf<Uri>() }
    val removedImageUrls = remember { mutableStateListOf<String>() }

    // Campos de texto para ubicación y detalles del producto
    var latitude by remember { mutableStateOf(TextFieldValue("")) }
    var longitude by remember { mutableStateOf(TextFieldValue("")) }
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var type by remember { mutableStateOf(TextFieldValue("")) }

    // Estados para mostrar errores de validación
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var typeError by remember { mutableStateOf(false) }

    // Control para mostrar diálogo de validación
    var showValidationDialog by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf("") }

    // Launcher para recortar imagen después de seleccionar
    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract(4f, 3f)
    ) { croppedUri ->
        croppedUri?.let {
            croppedImageUris.add(it)
        }
    }

    // Launcher para seleccionar imagen desde galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            cropImageLauncher.launch(it)
        }
    }

    LaunchedEffect(Unit) {
        // Obtener ubicación actual si los campos están vacíos
        if (latitude.text.isBlank() || longitude.text.isBlank()) {
            getCurrentLocation(context) { lat, lon ->
                latitude = TextFieldValue(lat.toString())
                longitude = TextFieldValue(lon.toString())
            }
        }
    }

    LaunchedEffect(productId) {
        if (!productId.isNullOrEmpty()) {
            viewModel.loadProductById(productId)
        } else {
            viewModel.clearSelectedProduct()
        }
    }

    product?.let { p ->
        LaunchedEffect(p) {
            // Cargar datos del producto en los campos
            title = TextFieldValue(p.title)
            description = TextFieldValue(p.description)
            price = TextFieldValue(p.price.toString())
            type = TextFieldValue(p.type)
        }

        val existingImages = remember {
            mutableStateListOf<String>().apply { addAll(p.product_picture ?: emptyList()) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = if (productId.isNullOrEmpty()) stringResource(R.string.create_product) else stringResource(R.string.edit_product),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (!productId.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.deleteProduct(p.id)
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.delete_product))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de texto con validación
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                label = { Text(stringResource(R.string.title_required)) },
                isError = titleError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
            )
            if (titleError) Text(stringResource(R.string.title_cannot_be_empty), color = Color.Red)

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = false
                },
                label = { Text(stringResource(R.string.description_required)) },
                isError = descriptionError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
            )
            if (descriptionError) Text(stringResource(R.string.description_cannot_be_empty), color = Color.Red)

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text(stringResource(R.string.price)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = type,
                onValueChange = {
                    type = it
                    typeError = false
                },
                label = { Text(stringResource(R.string.type_required)) },
                isError = typeError,
                modifier = Modifier.fillMaxWidth()
            )
            if (typeError) Text(stringResource(R.string.type_cannot_be_empty), color = Color.Red)

            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text(stringResource(R.string.latitude)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text(stringResource(R.string.longitude)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(R.string.product_images), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { pickImageLauncher.launch("image/*") }) {
                Text(
                    stringResource(R.string.select_images),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(existingImages) { index, url ->
                    Box(modifier = Modifier.size(120.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = stringResource(R.string.existing_image),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_image),
                            tint = Color.Red,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(24.dp)
                                .clickable {
                                    removedImageUrls.add(url)
                                    existingImages.removeAt(index)
                                }
                        )
                    }
                }

                itemsIndexed(croppedImageUris) { index, uri ->
                    Box(modifier = Modifier.size(120.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = stringResource(R.string.new_image),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_new_image),
                            tint = Color.Red,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(24.dp)
                                .clickable {
                                    croppedImageUris.removeAt(index)
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val isFormValid = title.text.isNotBlank() &&
                    description.text.isNotBlank() &&
                    type.text.isNotBlank()

            Button(
                onClick = {
                    val noImages = existingImages.isEmpty() && croppedImageUris.isEmpty()

                    when {
                        !isFormValid -> {
                            validationMessage = "Completa todos los campos obligatorios."
                            showValidationDialog = true
                        }

                        noImages -> {
                            validationMessage = "Debes subir al menos una imagen."
                            showValidationDialog = true
                        }

                        else -> {
                            val baseProduct = ProductDTO(
                                user_id = viewModel.currentUserId.value ?: "",
                                title = title.text,
                                description = description.text,
                                price = price.text.toDoubleOrNull() ?: 0.0,
                                type = type.text,
                                location = "POINT(${longitude.text} ${latitude.text})",
                                availability = p.availability,
                                product_picture = emptyList()
                            )

                            val finalProductId = if (p.id.isNotEmpty()) p.id else UUID.randomUUID().toString()

                            scope.launch {
                                val uploadedUrls = viewModel.uploadImagesForProduct(croppedImageUris, context, finalProductId)
                                val allUrls = existingImages + uploadedUrls

                                val finalProduct = baseProduct.copy(product_picture = allUrls)

                                if (p.id.isEmpty()) {
                                    viewModel.createProduct(finalProduct)
                                } else {
                                    viewModel.updateProduct(p.id, finalProduct)
                                }

                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text(if (productId.isNullOrEmpty()) stringResource(R.string.create_product) else stringResource(R.string.save_changes))
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    if (showValidationDialog) {
        AlertDialog(
            onDismissRequest = { showValidationDialog = false },
            confirmButton = {
                TextButton(onClick = { showValidationDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text(stringResource(R.string.validation)) },
            text = { Text(validationMessage) }
        )
    }
}
