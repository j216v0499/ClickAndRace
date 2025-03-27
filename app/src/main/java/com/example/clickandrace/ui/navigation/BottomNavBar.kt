package com.example.clickandrace.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Barra de navegación inferior personalizada que gestiona la navegación entre diferentes pantallas.
 *
 * @param navController El controlador de navegación que gestiona el estado y la navegación entre las pantallas.
 * @param isDarkMode Un valor booleano que indica si la aplicación está en modo oscuro.
 * @param sharedPreferences Referencia a las preferencias compartidas para recuperar datos del usuario.
 */

@Composable
fun BottomNavBar(
    //    isDarkMode: Boolean,
//    sharedPreferences: SharedPreferences
) {
//   val navBackStackEntry by navController.currentBackStackEntryAsState()
//
   // val currentDestination = navBackStackEntry?.destination
    val currentDestination = null;

//    // Cargar datos de usuario
//    val person = loadPersonFromPreferences(sharedPreferences)
//
    val displayName =  "pepe";
        //sharedPreferences.getString("user_name", person.name) ?: "Usuario"

//
//    // Usar el color adecuado dependiendo del tema (oscuro o claro)
    val navigationBarColor = if (true) {
        MaterialTheme.colorScheme.primary // Color para el modo oscuro
    } else {
        MaterialTheme.colorScheme.surface // Color para el modo claro
    }

    NavigationBar(
        containerColor = navigationBarColor // Cambiar color del fondo de la barra
    ) {
       // val navController = NavHostController;
//        AddNavItem("home", "Home", Icons.Default.Home, navController, currentDestination)
//        AddNavItem("maps", "Maps", Icons.Default.Place, navController, currentDestination)
//        AddNavItem("proyectos", "Proyectos", Icons.Default.Star, navController, currentDestination)
//        AddNavItem("marquetplace", "Market", Icons.Default.ShoppingCart, navController, currentDestination)
    //    AddNavItem("user", displayName.uppercase().trim(), Icons.Default.Person, navController, currentDestination)
    }
}

/**
 * Función Composable auxiliar para agregar un ítem a la barra de navegación.
 *
 * @param route La ruta asociada al destino de navegación.
 * @param label La etiqueta que se muestra en el ítem de navegación.
 * @param icon El ícono que representa el ítem en la barra de navegación.
 * @param navController El controlador de navegación.
 * @param currentDestination El destino actual en el que el usuario se encuentra navegando.
 *
 * Esta función permite configurar cada ítem de navegación, especificando el comportamiento
 * de navegación, los iconos y el texto, además de manejar la selección del ítem.
 */

@Composable
fun RowScope.AddNavItem(route: String, label: String, icon: ImageVector, navController: NavHostController, currentDestination: NavDestination?
) {
    val isSelected = currentDestination?.route == route

    NavigationBarItem(
        selected = isSelected,
        onClick = {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = { Icon(icon, contentDescription = null) },
        label = {
            Text(
                text = label,
                maxLines = 1, // Asegura que el texto se ajuste a una línea
                overflow = TextOverflow.Ellipsis // Si el texto es demasiado largo, se corta con "..."
            )
        }
    )
}