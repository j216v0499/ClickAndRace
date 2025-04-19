package com.dearos.clickandrace.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppNav(
//    navController: NavHostController,
//    isFirstLaunch: Boolean,
//    onSetupCompleted: (String, Boolean, Boolean) -> Unit,
    modifier: Modifier = Modifier,
//    sharedPreferences: SharedPreferences
) {

//    NavHost(
//        navController = navController,
//        startDestination = if (isFirstLaunch) "setup" else "home",
//        modifier = modifier
//    ) {
//
//        // Composable para configuración inicial
//     //   composable("setup") {
//            ConfiguracionScene { name, isAdult, prefersDarkTheme ->
//                onSetupCompleted(name, isAdult, prefersDarkTheme)
//
//                navController.navigate("home") {
//                    popUpTo("setup") { inclusive = true }
//                }
//
//                // Mostrar Toast después de la configuración
//
//                val welcomeMessage =
//                    "${if (isAdult) "Señor $name, sea usted bienvenido.\nVuesto" else
//                        " Hola $name .\nTu"}  tema preferido es ${if (prefersDarkTheme) "Oscuro" else "Claro"}."
//
//                Toast.makeText(this@MainActivity, welcomeMessage, Toast.LENGTH_LONG).show()}
//

     //   }
//
//        composable(
//            route = "home?name={name}",
//            arguments = listOf(
//                navArgument("name") {
//                    type = NavType.StringType
//                    defaultValue = "" // Valor por defecto si no se pasa argumento
//                }
//            )
//        ) { backStackEntry ->
//            val name = backStackEntry.arguments?.getString("name") ?: ""

//            HomeScene(
//                context = this@MainActivity,
//                sharedPreferences = sharedPreferences,
//                nombreUsuario = name
//            )
        }

//        composable("maps") {
//            MapasScene()
//        }
//
//        composable("proyectos") {
//            val proyectosRecientesImages = getDrawableResourcesByPrefix(this@MainActivity, "proyecto")
//            ImageScroll(proyectosRecientesImages)
//        }
//
//        composable("marquetplace") {
//            InventarioScreen(
//                database = database,
//            )
//        }
//        composable("user") {
//            UsuarioScren(
//                loadPersonFromPreferences(sharedPreferences),
//                onSavePerson = { person -> savePersonToPreferences(person, sharedPreferences) },
//                onLoadPerson = { loadPersonFromPreferences(sharedPreferences) },
//            )
//        }
//}