package com.dearos.clickandrace.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dearos.clickandrace.ui.components.BottomNavigationBar
import com.dearos.clickandrace.ui.components.WorkInProgressScreen
import com.dearos.clickandrace.ui.screens.appScreens.homePageAuthTest.HomeScreen
import com.dearos.clickandrace.ui.screens.appScreens.user.UserScreen

//TODO añadir documentacion

@Composable
fun AppNav(onLogout: () -> Unit
    ) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBar.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(BottomBar.EnLaZona.route) {
                // EnLaZonaScreen()
                //InTheZone()
                WorkInProgressScreen()

            }
            composable(BottomBar.Add.route) {
                // AddScreen()
                WorkInProgressScreen()

            }
            composable(BottomBar.Home.route) {
                HomeScreen(navigateToLogin = { },
                        onLogout = onLogout
                )
            }

            composable(BottomBar.MiLista.route) {
                // ListEverythingScreen()
                WorkInProgressScreen()

            }

//            composable(BottomBar.MiLista.route) {
//                // ListScreen()
//                WorkInProgressScreen()
//
//            }
            composable(BottomBar.Perfil.route) {
                // PerfilScreen()
                //WorkInProgressScreen()
                UserScreen()


            }
        }
    }
}

