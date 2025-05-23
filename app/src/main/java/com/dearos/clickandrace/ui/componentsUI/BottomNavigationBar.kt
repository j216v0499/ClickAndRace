package com.dearos.clickandrace.ui.componentsUI

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dearos.clickandrace.ui.navigation.BottomBar

/**
 * 'BottomNavigationBar' es un componente que representa la barra de navegación
 * inferior (Bottom Navigation Bar), permitiendo cambiar entre pantallas
 * principales de la app.
 *
 * @param navController Controlador de navegación usado para cambiar de pantalla.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Observa el destino actual en la pila de navegación
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Define el contenedor de la barra inferior
    // Define el contenedor de la barra inferior usando los colores del tema
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface, // Color de fondo de la barra de navegación
        contentColor = MaterialTheme.colorScheme.onSurface // Color de los iconos y texto en la barra
    ) {
        // Recorre todos los ítems definidos en BottomBar y crea un ícono por cada uno
        BottomBar.items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        // Navega a la ruta seleccionada y gestiona el backstack
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationRoute!!) {
                                saveState = true // Guarda el estado para restaurar luego
                            }
                            launchSingleTop = true // Evita múltiples instancias
                            restoreState = true // Restaura el estado anterior
                        }
                    }
                }
            )
        }
    }
}

/**
 * Vista previa de la barra de navegación inferior en el editor.
 * Se simula un `NavController` básico para mostrar la UI.
 */
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        BottomNavigationBar(navController = navController)
    }
}
