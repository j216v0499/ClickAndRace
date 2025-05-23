package com.dearos.clickandrace.ui.screens.appScreens.productsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import com.dearos.clickandrace.R

/**
 * Composable que representa la pantalla donde se muestran todos los productos disponibles.
 *
 * Esta pantalla permite:
 * - Ver una lista de productos cargados desde el ViewModel.
 * - Realizar búsquedas filtrando por título, descripción o tipo.
 * - Navegar a la pantalla de detalles de un producto específico al pulsar sobre él.
 *
 * @param navController Controlador de navegación para permitir el cambio de pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductsScreen(navController: NavController) {
    // ViewModel inyectado con Koin para acceder a los productos
    val viewModel: ProductViewModel = koinViewModel()

    // Estado observable con los productos cargados
    val products by viewModel.products.collectAsState()

    // Estado del campo de búsqueda (texto introducido por el usuario)
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Estado para mostrar u ocultar el campo de búsqueda
    var isSearchVisible by remember { mutableStateOf(false) }

    // Efecto lanzado una vez al iniciar el Composable para cargar productos
    LaunchedEffect(Unit) {
        viewModel.loadAllProducts()
    }

    // Estructura principal de la pantalla con AppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_all_products)) },
                actions = {
                    // Botón de búsqueda que muestra/oculta el campo de texto
                    IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.action_search)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Campo de búsqueda visible solo si isSearchVisible es true
            if (isSearchVisible) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(R.string.hint_search_products)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // Lista de productos filtrada por el texto introducido
            val filteredProducts = products.filter {
                it.title.contains(searchQuery.text, ignoreCase = true) ||
                        it.description.contains(searchQuery.text, ignoreCase = true) ||
                        it.type.contains(searchQuery.text, ignoreCase = true)
            }

            // Si no hay productos que coincidan con la búsqueda
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.message_no_products_available),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // Lista perezosa de productos ordenada de forma inversa (más recientes primero)
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredProducts.reversed()) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Al pulsar el producto se navega a sus detalles
                                    navController.navigate("product_details/${product.id}")
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                // Imagen del producto si existe

                                // Imagen con proporción 4:3 y tamaño fijo
                                val imageUrl = product.product_picture?.firstOrNull()
                                if (!imageUrl.isNullOrBlank()) {
                                    Box(
                                        modifier = Modifier
                                         //   .width(160.dp) // Puedes ajustar esto
                                            .fillMaxWidth(0.55f) // Ocupa el 30% del ancho de la fila (Row)

                                            .aspectRatio(4f / 3f) // Proporción 4:3 (ancho:alto)
                                            .padding(end = 16.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(imageUrl),
                                            contentDescription = stringResource(R.string.content_desc_product_image),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }

                                // Información del producto: título, precio, tipo y descripción resumida
                                Column(modifier = Modifier.weight(0.45f)) {
                                    Text(product.title, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        stringResource(R.string.text_price, product.price),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        stringResource(R.string.text_type, product.type),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        when {
                                            product.description.length > 40 ->
                                                stringResource(
                                                    R.string.text_description_short,
                                                    product.description.take(40)
                                                )

                                            product.description.isBlank() ->
                                                stringResource(R.string.text_no_description)

                                            else -> product.description
                                        },
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}