package com.dearos.clickandrace.ui.screens.authScreens.login

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dearos.clickandrace.LogsLogger
import com.dearos.clickandrace.R
import com.dearos.clickandrace.ui.componentsUI.LanguageSelector
import com.dearos.clickandrace.util.LanguageLocaleHelper
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla de inicio de sesión de la aplicación.
 *
 * Muestra los campos de correo y contraseña, gestiona eventos del ViewModel,
 * permite iniciar sesión con Google y redirige al usuario según el resultado.
 *
 * @param navigateToSignUp Función de navegación para ir al registro.
 * @param navigateToForgetPassword Función para ir a la pantalla de recuperación.
 * @param navigateToHome Función para ir a la pantalla principal tras iniciar sesión.
 * @param navigateToOtp Función para ir a la pantalla de verificación OTP si el email no está verificado.
 */

//Todo aplicar theme
@Composable
fun LoginScreen(
    navigateToSignUp: () -> Unit,
    navigateToForgetPassword: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToOtp: (String) -> Unit
) {
    // Estado para controlar la visibilidad de la contraseña
    var passwordVisible by remember { mutableStateOf(false) }

    // Obtener ViewModel inyectado por Koin
    val viewModel: LoginViewModel = koinViewModel()

    // Estado actual del formulario
    val state = viewModel.state
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    // Obtener la actividad actual del entorno Compose
    val activity = LocalActivity.current
        ?: throw IllegalStateException("LoginScreen debe estar alojada en una Activity")

    /**
     * Efecto que se activa cuando hay un evento de validación en el ViewModel.
     * Según el evento, se muestra un mensaje y se navega a la pantalla adecuada.
     */
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is LoginViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, context.getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }
                is LoginViewModel.ValidationEvent.EmailNotVerified -> {
                    Toast.makeText(context, context.getString(R.string.email_not_verified), Toast.LENGTH_LONG).show()
                    navigateToOtp(event.email)
                }
                is LoginViewModel.ValidationEvent.Failure -> {
                    LogsLogger.e("LoginUI.kt", "Error: ${event.error}")
                    val errorMessage = context.getString(R.string.login_error, event.error)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState) // <- scroll aquí
        ) {

        LanguageSelector { langCode ->
            LanguageLocaleHelper.setCurrentLanguage(context, langCode)
            LanguageLocaleHelper.setLocale(context, langCode)

            activity.recreate()

        }

        Spacer(modifier = Modifier.height(100.dp))

        // Título de bienvenida
        Text(
            text = stringResource(id = R.string.welcome),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        // Subtítulo
        Text(
            text = stringResource(id = R.string.intro_message),
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de correo electrónico
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(LoginFormEvent.EmailChanged(it)) },
            isError = state.emailError != null,
            shape = CircleShape,
            label = { Text(stringResource(id = R.string.email_address)) },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email Icon")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        // Mostrar error si existe
        if (state.emailError != null) {
            Text(
                text = state.emailError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 22.dp),
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña con botón para ver/ocultar
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(LoginFormEvent.PasswordChanged(it)) },
            isError = state.passwordError != null,
            shape = CircleShape,
            label = { Text(stringResource(id = R.string.password)) },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Password Icon")
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
                modifier = Modifier.padding(horizontal = 22.dp),
                fontSize = 13.sp
            )
        }

        // Enlace para recuperar contraseña
        Text(
            text = stringResource(id = R.string.forgot_password),
            color = Color(0xFF3A82F7),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(12.dp)
                .clickable { navigateToForgetPassword() }
        )

        // Botón para iniciar sesión
        Button(
            onClick = { viewModel.onEvent(LoginFormEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),

            ) {
            Text(text = stringResource(id = R.string.login_button), color = Color.White, fontWeight = FontWeight.Bold)
            if (state.isLoading) {
                Spacer(modifier = Modifier.width(10.dp))
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Sección para iniciar sesión con Google
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            // Texto ajustado sin cortes ni intros
            Text(
                text = stringResource(id = R.string.signin_with),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 1,
                softWrap = false, // <- Evita saltos de línea
                overflow = TextOverflow.Clip // <- No corta el texto
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }


        // Google Sign In Button
        OutlinedButton(
            onClick = { viewModel.onEvent(LoginFormEvent.GoogleSignIn(activity)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(23.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = R.string.google_login), color =  MaterialTheme.colorScheme.onBackground)

            if (state.isGoogleLoading) {
                Spacer(modifier = Modifier.width(10.dp))
                CircularProgressIndicator(
                    color = Color.Black, // or choose an appropriate color
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pie de pantalla con opción de registrarse
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.signup_question),
                    color =  MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.signup_action),
                    color = Color(0xFF3A82F7)       ,
                    modifier = Modifier.clickable { navigateToSignUp() }
                )
            }
        }
    }
    }
}