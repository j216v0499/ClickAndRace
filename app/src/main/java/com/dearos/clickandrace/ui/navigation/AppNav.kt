package com.dearos.clickandrace.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dearos.clickandrace.ui.componentsUI.BottomNavigationBar
import com.dearos.clickandrace.ui.componentsUI.WorkInProgressScreen
import com.dearos.clickandrace.ui.screens.appScreens.inTheZone.MapScreen
import com.dearos.clickandrace.ui.screens.appScreens.productsScreen.AllProductsScreen
import com.dearos.clickandrace.ui.screens.appScreens.productsScreen.CreateEditProductScreen
import com.dearos.clickandrace.ui.screens.appScreens.productsScreen.ProductDetailsScreen
import com.dearos.clickandrace.ui.screens.appScreens.profileScreen.UserProfileScreen
import com.dearos.clickandrace.ui.screens.appScreens.ratingScreen.RatingScreen

/**
 * `AppNav` es la función principal para manejar la navegación en la aplicación.
 * La función también gestiona la barra de navegación inferior y la transición entre pantallas.
 *
 * @param onLogout Función que se ejecuta cuando el usuario cierra sesión.
 */
@Composable
fun AppNav(onLogout: () -> Unit) {

    // Controlador de navegación para gestionar las pantallas y transiciones
    val navController = rememberNavController()

    // Scaffold con barra de navegación inferior que se renderiza en todas las pantallas
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // Barra de navegación en la parte inferior
    ) { innerPadding ->
        // Configuración del NavHost que gestiona la navegación entre las pantallas
        NavHost(
            navController = navController,
            startDestination = BottomBar.Home.route, // Pantalla inicial al abrir la app
            modifier = Modifier.padding(innerPadding) // Aplica el padding interno para evitar superposición con la barra
        ) {

            // Pantalla de "Home" que muestra la lista de productos
            composable(BottomBar.Home.route) {
                AllProductsScreen(navController = navController)
            }

            // Pantalla de "En la Zona", que muestra un mapa
            composable(BottomBar.EnLaZona.route) {
                MapScreen()
            }

            // Pantalla de "Agregar", para crear o editar productos
            composable(BottomBar.Add.route) {
                CreateEditProductScreen(navController = navController)
            }

            // Pantalla de "Mi Lista", que aún está en desarrollo
            composable(BottomBar.MiLista.route) {
                WorkInProgressScreen() // Muestra una pantalla de "En progreso"
            }

            // Pantalla de "Perfil", donde el usuario puede ver y editar su perfil
            composable(BottomBar.Perfil.route) {
                UserProfileScreen(onLogout = onLogout) // Se pasa la función de logout para cuando el usuario cierre sesión
            }

            // Pantalla de "Detalles del producto", con un ID de producto como parámetro
            composable("product_details/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                productId?.let {
                    // Navegar a los detalles de un producto específico
                    ProductDetailsScreen(productId = it, navController = navController)
                }
            }

            // Pantalla de "Editar producto", similar a la de detalles pero con funcionalidad para editar
            composable("edit_product/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                productId?.let {
                    // Navegar a la pantalla de edición de un producto específico
                    CreateEditProductScreen(productId = productId, navController = navController)
                }
            }

            // Pantalla de "Calificación de usuario", con IDs de usuario que califican y son calificados
            composable(
                route = "rate_user_screen/{raterId}/{ratedUserId}",
                arguments = listOf(
                    navArgument("raterId") { type = NavType.StringType },
                    navArgument("ratedUserId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                // Extraer los argumentos pasados a la pantalla
                val args = backStackEntry.arguments!!
                RatingScreen(
                    raterId = args.getString("raterId")!!, // ID del usuario que califica
                    ratedUserId = args.getString("ratedUserId")!!, // ID del usuario que está siendo calificado
                    navController = navController,
                )
            }
        }
    }
}
