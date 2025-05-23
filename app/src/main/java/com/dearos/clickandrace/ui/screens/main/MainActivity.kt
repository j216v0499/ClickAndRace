package com.dearos.clickandrace.ui.screens.main

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.ui.navigation.AppNav
import com.dearos.clickandrace.ui.navigation.AuthNav
import com.dearos.clickandrace.ui.navigation.AuthScreen
import com.dearos.clickandrace.ui.theme.ClickAndRaceTheme
import com.dearos.clickandrace.util.LanguageLocaleHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    // Inject the MainViewModel via Koin.
    private val mainViewModel: MainViewModel by viewModel()
    private var isAuthenticated by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val isDarkTheme = isSystemDarkThemeWithObserver()
            //isAuthenticated = remember { mutableStateOf(SessionManager.getSession(this)) }


            /// ClickAndRaceTheme(darkTheme = false, dynamicColor = false) {
            ClickAndRaceTheme(darkTheme = isDarkTheme){
                // Collect the UI state exposed by the ViewModel.
                val uiState by mainViewModel.uiState.collectAsState()

                ToastDebug(mainViewModel = mainViewModel)

                if (uiState.isLoading) {
                    LoadingScreen()
                } else {
                    if (isAuthenticated) {
                        AppNav(
                            onLogout = {
                                isAuthenticated = false
                            }
                        )
                    } else {
                        AuthNav(
                            startDestination = AuthScreen.Login.route,
                            onLoginSuccess = {
                                isAuthenticated = true
                            }
                        )
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = LanguageLocaleHelper.getCurrentLanguage(newBase)
        super.attachBaseContext(LanguageLocaleHelper.setLocale(newBase, lang))
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun isSystemDarkThemeWithObserver(): Boolean {
    val context = LocalContext.current
    val configuration = context.resources.configuration
    val uiMode = configuration.uiMode
    return (uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
}

/**
 * Composable that listens to the ViewModel’s toast events and shows a Toast message.
 */
@Composable
fun ToastDebug(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        mainViewModel.toastMessageFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}

