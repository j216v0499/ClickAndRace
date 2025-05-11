package com.dearos.clickandrace.ui.navigation

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object SignUp : AuthScreen("signup")
    object ResetPassword : AuthScreen("resetPassword")
    object SetNewPassword : AuthScreen("setNewPassword")
    object OtpVerification: AuthScreen("otpVerify")
    object HomeScreen: AuthScreen("homeScreen")
}
