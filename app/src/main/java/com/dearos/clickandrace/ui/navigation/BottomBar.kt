package com.dearos.clickandrace.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

//class BottomBar {
//}

sealed class BottomBar(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBar("homeApp", "Inicio", Icons.Default.Home)
    object EnLaZona : BottomBar("enLaZona", "Zona", Icons.Default.Place)
    object Add : BottomBar("add", "Agregar", Icons.Default.Add)
    object MiLista : BottomBar("miLista", "Mi Lista", Icons.Default.List)
    //Todo: Cambiar icono a foto de perfil
    object Perfil : BottomBar("perfil", "Perfil", Icons.Default.Person)

    companion object {
        val items = listOf(Home, EnLaZona, Add, MiLista, Perfil)
    }
}

