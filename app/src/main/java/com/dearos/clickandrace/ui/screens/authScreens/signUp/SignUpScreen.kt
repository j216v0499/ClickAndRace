package com.dearos.clickandrace.ui.screens.authScreens.signUp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.dearos.clickandrace.R
import org.koin.androidx.compose.koinViewModel

/**
 * Pantalla de registro de usuario.
 *
 * Permite al usuario crear una cuenta proporcionando nombre, correo, contraseña y aceptación de términos.
 * Valida la entrada, muestra errores y navega a la pantalla OTP si el registro es exitoso.
 *
 * @param navigateToLogin Función para navegar a la pantalla de inicio de sesión.
 * @param navigateToOtp Función para navegar a la pantalla de verificación OTP, recibe el email como parámetro.
 */
@Composable
fun SignUpScreen(
    navigateToLogin: () -> Unit,
    navigateToOtp: (String) -> Unit,
) {
    val viewModel: SignUpViewModel = koinViewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    val state = viewModel.stateFlow.collectAsState().value
    val context = LocalContext.current

    // Escucha eventos del ViewModel para navegar en caso de éxito
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            if (event is SignUpViewModel.ValidationEvent.Success) {
                navigateToOtp(event.email)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = stringResource(R.string.register_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Subtítulo
        Text(
            text = stringResource(R.string.register_subtitle),
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        // Campo nombre
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(SignUpFormEvent.NameChanged(it)) },
            isError = state.nameError != null,
            shape = CircleShape,
            label = { Text(stringResource(R.string.full_name)) },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        state.nameError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 22.dp),
                fontSize = 13.sp
            )
        }

        // Campo email
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(SignUpFormEvent.EmailChanged(it)) },
            isError = state.emailError != null,
            shape = CircleShape,
            label = { Text(stringResource(R.string.email_address)) },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        state.emailError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 22.dp),
                fontSize = 13.sp
            )
        }

        // Campo contraseña
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(SignUpFormEvent.PasswordChanged(it)) },
            isError = state.passwordError != null,
            shape = CircleShape,
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = null)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        state.passwordError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 22.dp),
                fontSize = 13.sp
            )
        }

        // Aceptar términos
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.acceptedTerms,
                onCheckedChange = { viewModel.onEvent(SignUpFormEvent.AcceptTerms(it)) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Gray,
                    uncheckedColor = Color(0xFF9C9EA1)
                )
            )
            Text(text = stringResource(R.string.agree_to), color = Color(0xFF9C9EA1), fontSize = 15.sp)
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 15.sp,
                color = Color(0xFF3A82F7),
                modifier = Modifier.clickable { /* TODO si queremos redireccionar a algun sitio */ }
            )
        }
        state.termsError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 14.dp),
                fontSize = 13.sp
            )
        }

        // Botón de registro
        Button(
            onClick = { viewModel.onEvent(SignUpFormEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0e5ef5))
        ) {
            if (state.isLoading) {
                Text(text = stringResource(R.string.sign_up), color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(text = stringResource(R.string.sign_up), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // División horizontal con texto "O"
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

        // Botón de Google
        OutlinedButton(
            onClick = { /* TODO: Google Sign-In */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(23.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(R.string.google_login), color = Color.Black)
        }

        // Enlace a iniciar sesión
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
                Text(text = stringResource(R.string.already_have_account))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.sign_in),
                    color = Color(0xFF3A82F7),
                    modifier = Modifier.clickable { navigateToLogin() }
                )
            }
        }
    }
}
