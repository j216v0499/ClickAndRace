package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.setNew

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dearos.clickandrace.R
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla para establecer una nueva contraseña.
 *
 * @param navigateToLogin Función para navegar a la pantalla de login tras éxito.
 * @param navigateToReset Función para navegar a la pantalla anterior de reset password.
 */
@Composable
fun SetPasswordScreen(
    navigateToLogin: () -> Unit,
    navigateToReset: () -> Unit
) {

    // Estados para visibilidad de contraseñas
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Obtener ViewModel con Koin
    val viewModel: SetPasswordViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    /**
     * Escuchar eventos de validación (éxito al resetear contraseña)
     * para mostrar Toast y navegar a Login.
     */
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is SetPasswordViewModel.ValidationEvent.Success -> {

                    // Eliminar flag de contraseña pendiente en preferencias
                    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().remove("pending_reset_password").apply()

                    Toast.makeText(context, "Password Reset Successful", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                IconButton(
                    onClick = { navigateToReset() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.action_back),
                        tint = Color.Black
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            // Título principal
            Text(
                text = stringResource(R.string.set_new_password_title),
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF02040b)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtítulo explicativo
            Text(
                text = stringResource(R.string.reset_password_subtitle),
                fontSize = 15.sp,
                color = Color(0xFF767a7b)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo para la nueva contraseña
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(SetPasswordFormEvent.PasswordChanged(it)) },
                isError = state.passwordError != null,
                shape = CircleShape,
                label = { Text(stringResource(R.string.password_label)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // Mostrar error si existe
            if (state.passwordError != null) {
                Text(
                    text = state.passwordError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para confirmar la contraseña
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onEvent(SetPasswordFormEvent.ConfirmPasswordChanged(it)) },
                isError = state.confirmPasswordError != null,
                shape = CircleShape,
                label = { Text(stringResource(R.string.confirm_password_label)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // Mostrar error si existe
            if (state.confirmPasswordError != null) {
                Text(
                    text = state.confirmPasswordError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(27.dp))

            // Botón para enviar el formulario de nueva contraseña
            Button(
                onClick = { viewModel.onEvent(SetPasswordFormEvent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0e5ef5))
            ) {
                Text(stringResource(R.string.password_reset_successful))
            }
        }
    }
}
