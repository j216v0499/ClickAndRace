package com.dearos.clickandrace.ui.navigation

/**
 * `AuthScreen` es una clase sellada que define las diferentes pantallas o rutas de la parte de autenticación de la aplicación.
 *
 * Cada objeto dentro de esta clase representa una pantalla en el flujo de autenticación, como inicio de sesión,
 * registro, recuperación de contraseña, etc.
 *
 * Esta implementación utiliza el patrón de diseño de clases selladas para asegurar que solo haya un conjunto específico
 * de pantallas en la navegación de autenticación, evitando errores y mejorando la claridad del código.
 */
sealed class AuthScreen(val route: String) {

    /**
     * Representa la pantalla de inicio de sesión.
     * - Ruta: "login"
     */
    object Login : AuthScreen("login")

    /**
     * Representa la pantalla de registro de un nuevo usuario.
     * - Ruta: "signup"
     */
    object SignUp : AuthScreen("signup")

    /**
     * Representa la pantalla de recuperación de contraseña.
     * - Ruta: "resetPassword"
     */
    object ResetPassword : AuthScreen("resetPassword")

    /**
     * Representa la pantalla para establecer una nueva contraseña luego de un cambio.
     * - Ruta: "setNewPassword"
     */
    object SetNewPassword : AuthScreen("setNewPassword")

    /**
     * Representa la pantalla de verificación por OTP (One Time Password).
     * - Ruta: "otpVerify"
     */
    object OtpVerification: AuthScreen("otpVerify")

    /**
     * Representa la pantalla principal (o la pantalla que aparece después de un inicio de sesión exitoso).
     * - Ruta: "homeScreen"
     */
    object HomeScreen: AuthScreen("homeScreen")
}
