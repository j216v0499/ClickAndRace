package com.dearos.clickandrace.ui.screens.authScreens.otpVerification

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dearos.clickandrace.R
import org.koin.androidx.compose.koinViewModel


/**
 * Pantalla de verificación de OTP.
 *
 * Se encarga de:
 * - Mostrar la interfaz para ingresar un código OTP.
 * - Gestionar el enfoque automático entre campos.
 * - Verificar el OTP ingresado.
 * - Gestionar la navegación y feedback visual.
 *
 * @param email Dirección de correo electrónico del usuario.
 * @param flow Indica si la pantalla se abrió por flujo de registro o recuperación.
 * @param navigateAfterOtp Navega a la siguiente pantalla si el OTP es válido.
 * @param onBackPressed Acción para volver atrás.
 */

@Composable
fun OtpVerificationScreen(
    email: String,
    flow: String,
    navigateAfterOtp: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.action_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    ) { innerPadding ->

        // ViewModel inyectado con Koin
        val viewModel: OtpViewModel = koinViewModel()

        // Observa el estado de la pantalla
        val state by viewModel.state.collectAsStateWithLifecycle()

        // Crea los FocusRequesters para los campos OTP
        val focusRequesters = remember { List(6) { FocusRequester() } }

        val focusManager = LocalFocusManager.current
        val keyboardManager = LocalSoftwareKeyboardController.current
        val context = LocalContext.current

        // Enfoca automáticamente el campo según el índice actual
        LaunchedEffect(state.focusedIndex) {
            state.focusedIndex?.let { index ->
                focusRequesters.getOrNull(index)?.requestFocus()
            }
        }

        // Oculta el teclado y limpia el enfoque cuando todos los números se han introducido
        LaunchedEffect(state.code, keyboardManager) {
            val allNumbersEntered = state.code.none { it == null }
            if (allNumbersEntered) {
                focusRequesters.forEach { it.freeFocus() }
                focusManager.clearFocus()
                keyboardManager?.hide()
            }
        }

        // Si el flujo es de recuperación de contraseña, marca la bandera
        LaunchedEffect(key1 = flow) {
            if (flow == "reset") {
                val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("pending_reset_password", true).apply()
            }
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = innerPadding.calculateTopPadding())
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            // Título
            Text(
                text = stringResource(R.string.otp_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo
            Text(
                text = stringResource(R.string.otp_subtitle, email),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Zona de entrada OTP
            OtpScreen(
                state = state,
                focusRequesters = focusRequesters,
                onAction = { action ->
                    if (action is OtpAction.OnEnterNumber && action.number != null) {
                        focusRequesters[action.index].freeFocus()
                    }
                    viewModel.onAction(action)
                }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón para verificar
            Button(
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                onClick = {
                    viewModel.verifyOtp(email)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = CircleShape
            ) {
                Text(text = stringResource(R.string.otp_button_verify), fontSize = 16.sp, color = Color.White)
            }

            // Resultado de la validación del OTP
            when (state.isValid) {
                true -> {
                    LaunchedEffect(Unit) {
                        navigateAfterOtp()
                    }
                }

                false -> {
                    Text(stringResource(R.string.otp_invalid_error), color = Color.Red)
                }

                null -> Unit // Esperando validación
            }
        }
    }
}
