package com.example.clickandrace

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.clickandrace.ui.AppNav
import com.example.clickandrace.ui.navigation.BottomNavBar
import com.example.clickandrace.ui.theme.ClickAndRaceTheme
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth  // Instancia de Firebase Auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Iniciar sesión de prueba anónima
        signInAnonymously()

        //Analitic event
        // Inicializar Firebase Analytics
        val analytics2 = Firebase.analytics
        val bundle2 = Bundle().apply {
            putString("message", "Integración de Firebase correcta")
        }
        analytics2.logEvent("init_screen", bundle2)



        // Inicializar Firebase Analytics
        val analytics = Firebase.analytics

        // Verificar inicialización
        if (analytics != null) {
            Log.d("FirebaseAnalytics", "Firebase Analytics inicializado correctamente")
        } else {
            Log.e("FirebaseAnalytics", "Error al inicializar Firebase Analytics")
        }

        // Enviar evento de prueba
        val bundle = Bundle().apply {
            putString("message", "Integración de Firebase correcta")
        }
        analytics.logEvent("init_screen", bundle)
        //Firebase.analytics.logEvent()



        setContent {
            ClickAndRaceTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )




                    Scaffold(
                        bottomBar = {
                            /*  currentBackStackEntryAsState() convierte el estado actual del back stack (pilas de navegación) en un State observable por Compose.
                                value?.destination recupera el destino actual (la pantalla en la que estamos) del estado de navegación */

                                BottomNavBar( )

                        }
                    )
                    {
                        padding ->

                        // Llamar a AppNavigation
                        AppNav(
//                            navController = navController,
//                            isFirstLaunch = isFirstLaunch,
//                            onSetupCompleted = { name, isAdult, prefersDarkTheme ->
//                                val updatedPerson = Person(name, isAdult, prefersDarkTheme, true)
//                                isDarkTheme = prefersDarkTheme
//                                savePersonToPreferences(updatedPerson, sharedPreferences)
//                                isFirstLaunch = false
//                            },
                              modifier = Modifier.padding(padding),
//                            sharedPreferences = sharedPreferences
                        )
                    }


                }
            }
        }


private fun signInAnonymously() {
    auth.signInAnonymously()
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                Log.d("FirebaseAuth", "Autenticado como: ${user?.uid}")
            } else {
                Log.e("FirebaseAuth", "Error al autenticar", task.exception)
            }
        }
}
}


//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!,App test C.A.R.",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ClickAndRaceTheme {
//        Greeting("Android")
//    }
//}