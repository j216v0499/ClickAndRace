package com.dearos.clickandrace.ui.navigation

import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dearos.clickandrace.ui.screens.*


//TODO añadir documentacion
@Composable
fun AppNav() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {

                val navBackStackEntry = navController.currentBackStackEntry
                val currentRoute = navBackStackEntry?.destination?.route
               // val currentRoute = currentBackStackEntryAsState().value?.destination?.route
                BottomBar.items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = { navController.navigate(screen.route) },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) {

        //TODO; VINCULAR LAS PANTALLAS CON EL NAVCONTROLLER

        innerPadding ->


        NavHost(
            navController = navController,
            startDestination = BottomBar.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(BottomBar.Home.route) {
                //Carpeta creada
               // HomeAppScreen()
            }
            composable(BottomBar.EnLaZona.route) {
                //Carpeta creada
                // EnLaZonaScreen()
            }
            composable(BottomBar.Add.route) {
                //Carpeta creada
                //  AddSomthingScreen()
            }
            composable(BottomBar.MiLista.route) {
               // personal list()
            }
            composable(BottomBar.Perfil.route) {
              //  profileScreen()
            }
        }
    }
}
