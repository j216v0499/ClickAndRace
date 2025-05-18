package com.dearos.clickandrace.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * `BottomBar` es una clase sellada que define los diferentes elementos de la barra de navegación inferior
 * de la aplicación. Cada objeto dentro de esta clase representa una pestaña de la barra de navegación.
 *
 * Cada elemento tiene una ruta (`route`), un título (`title`) que se muestra al usuario,
 * y un icono (`icon`) que se usa para representar visualmente la pestaña.
 *
 * Esta clase aprovecha el patrón de diseño de clase sellada para limitar las opciones a solo las pestañas
 * definidas dentro de la clase, lo que facilita el control y evita errores.
 */
sealed class BottomBar(val route: String, val title: String, val icon: ImageVector) {

    /**
     * Representa la pestaña de inicio en la barra de navegación.
     * - Ruta: "homeApp"
     * - Título: "Inicio"
     * - Icono: ícono de inicio (home).
     */
    object Home : BottomBar("homeApp", "Inicio", Icons.Default.Home)

    /**
     * Representa la pestaña de la zona en la barra de navegación.
     * - Ruta: "enLaZona"
     * - Título: "Zona"
     * - Icono: ícono de ubicación (place).
     */
    object EnLaZona : BottomBar("enLaZona", "Zona", Icons.Default.Place)

    /**
     * Representa la pestaña para agregar contenido en la barra de navegación.
     * - Ruta: "add"
     * - Título: "Agregar"
     * - Icono: ícono de añadir (add).
     */
    object Add : BottomBar("add", "Agregar", Icons.Default.Add)

    /**
     * Representa la pestaña de la lista del usuario en la barra de navegación.
     * - Ruta: "miLista"
     * - Título: "Mi Lista"
     * - Icono: ícono de lista (list).
     */
    object MiLista : BottomBar("miLista", "Mi Lista", Icons.AutoMirrored.Filled.List)

    /**
     * Representa la pestaña del perfil del usuario en la barra de navegación.
     * - Ruta: "perfil"
     * - Título: "Perfil"
     * - Icono: ícono de perfil (person).
     */
    object Perfil : BottomBar("perfil", "Perfil", Icons.Default.Person)

    /**
     * Objeto companion que contiene la lista de todos los elementos de la barra de navegación.
     * Esta lista es útil para acceder a todos los elementos de la barra de navegación de manera centralizada.
     */
    companion object {
        val items = listOf(Home, EnLaZona, Add, MiLista, Perfil)
    }
}
