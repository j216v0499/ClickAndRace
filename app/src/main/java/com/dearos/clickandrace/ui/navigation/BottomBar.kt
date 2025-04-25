package com.dearos.clickandrace.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.libraries.places.api.model.Place

//class BottomBar {
//}

//TODO añadir documetnacion
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

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object SignUp : AuthScreen("signup")

    object ResetPassword : AuthScreen("resetPassword")
    object SetNewPassword : AuthScreen("setNewPassword")

    object OtpVerification: AuthScreen("otpVerify")
    object HomeScreen: AuthScreen("homeScreen")
}
