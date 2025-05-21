package com.dearos.clickandrace.ui.screens.authScreens.forgetPassword.reset

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dearos.clickandrace.R
import org.koin.androidx.compose.koinViewModel

/**
 * Composable para la pantalla de restablecer contraseña.
 *
 * El usuario ingresa su correo electrónico para recibir un código OTP.
 *
 * @param navigateToLogin Navegación hacia la pantalla de inicio de sesión.
 * @param navigateToOtp Navegación hacia la pantalla OTP, recibe el email como argumento.
 */
@Composable
fun ForgetPasswordScreen(
    navigateToLogin: () -> Unit,
    navigateToOtp: (String) -> Unit
) {
    val viewModel: ResetPasswordViewModel = koinViewModel()
    val state = viewModel.stateFlow.collectAsState().value
    val context = LocalContext.current
    val otpSentSuccess = stringResource(R.string.otp_sent_success)

    // Escucha eventos de validación emitidos por el ViewModel
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ResetPasswordViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, otpSentSuccess, Toast.LENGTH_SHORT).show()
                    navigateToOtp(event.email)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                IconButton(onClick = { navigateToLogin() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.action_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            Text(
                text = stringResource(R.string.reset_password_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.reset_password_subtitle),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(ForgetPasswordFormEvent.EmailChanged(it)) },
                isError = state.emailError != null,
                shape = CircleShape,
                label = { Text(stringResource(R.string.email_address)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = stringResource(R.string.email_icon)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (state.emailError != null) {
                Text(
                    text = state.emailError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 22.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEvent(ForgetPasswordFormEvent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.send_otp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))

                if (state.isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
