package com.dearos.clickandrace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dearos.clickandrace.ui.screens.appScreens.homeScreen.HomeScreen
import com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.reset.ForgetPasswordScreen
import com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.setNew.SetPasswordScreen
import com.dearos.clickandrace.ui.screens.authScreens.login.LoginScreen
import com.dearos.clickandrace.ui.screens.authScreens.otpVerification.OtpVerificationScreen
import com.dearos.clickandrace.ui.screens.authScreens.signUp.SignUpScreen

/**
 * `AuthNav` es una función de navegación principal que configura las pantallas y transiciones
 * entre las pantallas del flujo de auth de la aplicación.
 *
 * @param startDestination La ruta de la pantalla inicial (por ejemplo, Login) a la que navegar.
 * @param onLoginSuccess Función que se ejecuta cuando el inicio de sesión es exitoso.
 */

@Composable
fun AuthNav(
    startDestination: String,
    onLoginSuccess: () -> Unit
) {
    // Controlador de navegación que maneja el flujo entre pantallas
    val navController = rememberNavController()

    // Definición del NavHost que manejará las rutas de navegación de la aplicación
    NavHost(navController = navController, startDestination = startDestination) {

        // Pantalla de Login
        composable(
            route = AuthScreen.Login.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Componente de pantalla de Login con rutas para navegar a otras pantallas
            LoginScreen(
                navigateToSignUp = { navController.navigate(AuthScreen.SignUp.route) },
                navigateToForgetPassword = { navController.navigate(AuthScreen.ResetPassword.route) },
                navigateToHome = {
                    // Navegar a la pantalla de inicio y limpiar el backstack de la navegación
                    navController.navigate(AuthScreen.HomeScreen.route) {
                        popUpTo(AuthScreen.Login.route) { inclusive = true }
                    }
                    onLoginSuccess()  // Ejecuta la función pasada al iniciar sesión con éxito
                },
                navigateToOtp = { email ->
                    // Navegar a la pantalla de verificación OTP con el email como parámetro
                    navController.navigate("${AuthScreen.OtpVerification.route}/$email/login")
                }
            )
        }

        // Pantalla de SignUp
        composable(
            route = AuthScreen.SignUp.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Pantalla de registro con rutas de navegación
            SignUpScreen(
                navigateToLogin = { navController.navigate(AuthScreen.Login.route) },
                navigateToOtp = { email ->
                    // Navegar a la pantalla de verificación OTP desde el registro
                    navController.navigate("${AuthScreen.OtpVerification.route}/$email/signup")
                }
            )
        }

        // Pantalla de OTP (Verificación)
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
            // Extraer los argumentos enviados a la pantalla de OTP
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val flow = backStackEntry.arguments?.getString("flow") ?: "signup"

            // Componente de pantalla de verificación OTP
            OtpVerificationScreen(
                email = email,
                flow = flow,
                navigateAfterOtp = {
                    when (flow) {
                        "login" -> {
                            // Navegar a la pantalla principal después de OTP para login
                            navController.navigate(AuthScreen.HomeScreen.route) {
                                popUpTo(AuthScreen.Login.route) { inclusive = true }
                            }
                        }
                        "reset" -> {
                            // Navegar a la pantalla de restablecer contraseña después de OTP
                            navController.navigate(AuthScreen.SetNewPassword.route) {
                                popUpTo(AuthScreen.ResetPassword.route) { inclusive = true }
                            }
                        }
                        else -> {
                            // Navegar a la pantalla de login después de OTP para registro
                            navController.navigate(AuthScreen.Login.route) {
                                popUpTo(AuthScreen.Login.route) { inclusive = true }
                            }
                        }
                    }
                },
                onBackPressed = { navController.popBackStack() }
            )
        }

        // Pantalla de Restablecimiento de Contraseña
        composable(
            route = AuthScreen.ResetPassword.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Componente de pantalla de restablecimiento de contraseña
            ForgetPasswordScreen(
                navigateToLogin = { navController.navigate(AuthScreen.Login.route) },
                navigateToOtp = { email ->
                    // Navegar a la pantalla de OTP desde restablecer contraseña
                    navController.navigate("${AuthScreen.OtpVerification.route}/$email/reset")
                }
            )
        }

        // Pantalla de Establecer Nueva Contraseña
        composable(
            route = AuthScreen.SetNewPassword.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Componente de pantalla para establecer una nueva contraseña
            SetPasswordScreen(
                navigateToLogin = {
                    // Navegar a la pantalla de Login y limpiar el backstack
                    navController.navigate(AuthScreen.Login.route) {
                        popUpTo(0)  // Limpiar all the el backstack
                    }
                },
                navigateToReset = { navController.navigate(AuthScreen.ResetPassword.route) }
            )
        }

        // Pantalla de Inicio (Home)
        composable(
            route = AuthScreen.HomeScreen.route,
            enterTransition = { transicionDeEntrada() },
            exitTransition = { transicionDeSalida() },
            popEnterTransition = { transicionPopDeEntrada() },
            popExitTransition = { transicionPopDeSalida() }
        ) {
            // Componente de pantalla principal
            HomeScreen(
                navigateToLogin = {
                    // Navegar a la pantalla de Login y limpiar el backstack
                    navController.navigate(AuthScreen.Login.route) {
                        popUpTo(0)  // Limpiar all the el backstack
                    }
                },
                onLogout = {/*No se hace nada en este caso */}
            )
        }
    }
}
