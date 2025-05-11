package com.dearos.clickandrace.ui.screens.appScreens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dearos.clickandrace.data.model.User
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserScreen() {
    val viewModel: UserViewModel = koinViewModel() // Usar getViewModel() de Koin

    val authState by viewModel.authState.collectAsState()
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (authState) {
            is AuthState.Authenticated -> {
                if (user != null) {
                    Row {
                        UserCard(user!!)
                    }
                    Button(onClick = { viewModel.logout() }) {
                        Text("Cerrar sesión")
                    }
                } else {
                    CircularProgressIndicator() // o cualquier UI de espera
                }
                Button(onClick = { viewModel.logout() }) {
                    Text("Cerrar sesión")
                }
            }

            AuthState.Unauthenticated -> {
                // Formulario de inicio de sesión o Google
                // ... (mismo código anterior)
            }

            AuthState.Loading -> CircularProgressIndicator()
        }
    }
}

@Composable
private fun UserCard(user: User) {
    Card(
        modifier = Modifier.padding(16.dp)

    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("ID: ${user.id}")
            Text("Nombre: ${user.name}")
            Text("Email: ${user.email}")
            Text("Rating: ${user.rating}")
        }
    }
}