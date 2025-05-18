package com.dearos.clickandrace.ui.screens.appScreens.homeScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dearos.clickandrace.R
import com.dearos.clickandrace.ui.componentsUI.LanguageSelector
import com.dearos.clickandrace.util.LanguageLocaleHelper
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla principal del usuario autenticado.
 *
 * Esta pantalla muestra un mensaje de bienvenida, los tokens actuales (por motivos de debug),
 * un selector de idioma, y un botón para cerrar sesión. También gestiona estados de carga y error.
 *
 * @param navigateToLogin Función de navegación que puede usarse para redirigir al usuario a la pantalla de login.
 * @param onLogout Callback que se ejecuta después de un logout exitoso.
 * @param homeViewModel ViewModel de la pantalla, inyectado por Koin. Contiene el estado de UI y lógica de logout.
 */
@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    onLogout : () -> Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Si hay un mensaje de error, se muestra mediante un Toast
    uiState.errorMessage?.let { error ->
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_LONG).show()
    }

    // Si está cargando, mostrar indicador centrado
    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Mensaje de bienvenida personalizado
            Text(
                text = "${stringResource(id = R.string.welcome)} ${uiState.userName}! 🎉",
                fontSize = 24.sp,
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar tokens guardados (debug visual)
            Text(
                text = "Access Token: ${uiState.accessToken}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Refresh Token: ${uiState.refreshToken}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Selector de idiomas
            LanguageSelector { langCode ->
                LanguageLocaleHelper.setCurrentLanguage(context, langCode)
                LanguageLocaleHelper.setLocale(context, langCode)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Botón de logout
            Button(
                onClick = {
                    coroutineScope.launch {
                        homeViewModel.logout {
                            onLogout() // Llamado solo si el logout fue exitoso
                            // navigateToLogin()
                            // comentado, por si se decide navegar manualmente
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text("Logout", color = Color.White)
            }
        }
    }
}
