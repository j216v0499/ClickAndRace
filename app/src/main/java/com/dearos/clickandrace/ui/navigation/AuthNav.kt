package com.dearos.clickandrace.ui.navigation

//import com.dearos.auth.homePage.HomeScreen
//import com.dearos.ui.auth.presentation.forgetPassword.reset.ForgetPasswordScreen
//import com.dearos.ui.auth.presentation.forgetPassword.setNew.SetPasswordScreen
//import com.dearos.ui.auth.presentation.login.LoginScreen
//import com.dearos.ui.auth.presentation.otpVerification.OtpVerificationScreen
//import com.dearos.ui.auth.presentation.signUp.SignUpScreen


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dearos.clickandrace.ui.screens.appScreens.homePageAuthTest.HomeScreen
import com.dearos.clickandrace.ui.screens.authScreens.login.LoginScreen

/**
 * Función de navegación principal que configura las pantallas y transiciones
 * entre ellas. Esta función maneja la navegación de toda la aplicación de autenticación,
 * incluyendo Login, SignUp, OTP, Restablecer Contraseña, y la pantalla de inicio.
 *
 * @param startDestination La ruta de la pantalla inicial a la que navegar.
 */



@Composable
fun AuthNav(
    startDestination: String,
    onLoginSuccess: () -> Unit
) {
    // Controlador de navegación para manejar el flujo entre pantallas
    val navController = rememberNavController()

    // Definición del NavHost que maneja todas las rutas
    NavHost(navController = navController, startDestination = startDestination) {

        // Pantalla de Login
        composable(
            route = AuthScreen.Login.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Pantalla de Login con las respectivas rutas de navegación
            LoginScreen(
                navigateToSignUp = { navController.navigate(AuthScreen.SignUp.route) },
                navigateToForgetPassword = { navController.navigate(AuthScreen.ResetPassword.route) },
                navigateToHome = {
                    // Navegar a la pantalla de inicio y limpiar el backstack
                    navController.navigate(AuthScreen.HomeScreen.route) {
                        popUpTo(AuthScreen.Login.route) { inclusive = true }
                    }
                     onLoginSuccess()  // Aquí

                },
                navigateToOtp = { email ->
                    // Navegar a la pantalla de verificación OTP
                    navController.navigate("${AuthScreen.OtpVerification.route}/$email/login")
                }
            )
        }

        // Pantalla de Sign-Up
        composable(
            route = AuthScreen.SignUp.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Pantalla de Sign-Up con rutas de navegación relacionadas
//            SignUpScreen(
//                navigateToLogin = { navController.navigate(AuthScreen.Login.route) },
//                navigateToOtp = { email ->
//                    // Navegar a la pantalla de verificación OTP desde el SignUp
//                    navController.navigate("${AuthScreen.OtpVerification.route}/$email/signup")
//                }
//            )
        }

        // Pantalla de OTP (verificación)
        composable(
            route = "${AuthScreen.OtpVerification.route}/{email}/{flow}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("flow") { type = NavType.StringType }
            ),
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) { backStackEntry ->
            // Obtener los argumentos de la pantalla de OTP (email y flujo)
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val flow = backStackEntry.arguments?.getString("flow") ?: "signup"

            // Pantalla de verificación OTP con lógica de navegación posterior a la verificación
//            OtpVerificationScreen(
//                email = email,
//                flow = flow,
//                navigateAfterOtp = {
//                    when (flow) {
//                        "login" -> {
//                            // Después de la verificación OTP para Login, navegar a Home
//                            navController.navigate(AuthScreen.HomeScreen.route) {
//                                popUpTo(AuthScreen.Login.route) { inclusive = true }
//                            }
//                        }
//                        "reset" -> {
//                            // Después de la verificación OTP para Reset, navegar a SetPassword
//                            navController.navigate(AuthScreen.SetNewPassword.route) {
//                                popUpTo(AuthScreen.ResetPassword.route) { inclusive = true }
//                            }
//                        }
//                        else -> {
//                            // Después de la verificación OTP para SignUp, navegar a Login
//                            navController.navigate(AuthScreen.Login.route) {
//                                popUpTo(AuthScreen.Login.route) { inclusive = true }
//                            }
//                        }
//                    }
//                },
//                onBackPressed  = { navController.popBackStack() }
//            )
        }

        // Pantalla de Restablecimiento de Contraseña
        composable(
            route = AuthScreen.ResetPassword.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Pantalla de olvido de contraseña
//            ForgetPasswordScreen(
//                navigateToLogin = { navController.navigate(AuthScreen.Login.route) },
//                navigateToOtp = { email ->
//                    // Navegar a la pantalla de OTP desde el flujo de restablecimiento
//                    navController.navigate("${AuthScreen.OtpVerification.route}/$email/reset")
//                }
//            )
        }

        // Pantalla de establecer nueva contraseña
        composable(
            route = AuthScreen.SetNewPassword.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ){
            // Pantalla de establecer nueva contraseña con navegación a login o restablecer contraseña
//            SetPasswordScreen(
//                navigateToLogin = {
//                    navController.navigate(AuthScreen.Login.route) {
//                        popUpTo(0) // Limpiar el backstack
//                    }
//                },
//                navigateToReset = { navController.navigate(AuthScreen.ResetPassword.route) }
//            )
        }

        // Pantalla de inicio (Home)
        composable(
            route = AuthScreen.HomeScreen.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Pantalla de inicio con navegación hacia Login
            HomeScreen(
                navigateToLogin = {
                    navController.navigate(AuthScreen.Login.route) {
                        popUpTo(0) // Limpiar el backstack si es necesario
                    }
                },
                onLogout = {}
            )
        }
    }
}
