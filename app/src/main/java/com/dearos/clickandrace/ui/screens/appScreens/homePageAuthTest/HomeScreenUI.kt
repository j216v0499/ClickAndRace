package com.dearos.clickandrace.ui.screens.appScreens.homePageAuthTest

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
import com.dearos.clickandrace.ui.components.LanguageSelector
import com.dearos.clickandrace.util.LanguageLocaleHelper
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    onLogout : () -> Unit,

    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // If there's an error message, show a Toast (or display it in the UI)
    uiState.errorMessage?.let { error ->
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_LONG).show()
    }

    // Check if we're in a loading state
    if (uiState.isLoading) {
        // Show a centered loading indicator
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
            // Welcome Message
            Text(
                text = "${stringResource(id = R.string.welcome)} ${uiState.userName}! 🎉",
                fontSize = 24.sp,
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Visual Debug: Display the stored tokens
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

            LanguageSelector { langCode ->
                LanguageLocaleHelper.setCurrentLanguage(context, langCode)
                LanguageLocaleHelper.setLocale(context, langCode)

            }

            Spacer(modifier = Modifier.height(30.dp))

            //Logout Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        homeViewModel.logout {

                            onLogout() // <- solo llamas esto si el logout fue exitoso


                           // navigateToLogin()
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
