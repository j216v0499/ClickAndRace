package com.dearos.clickandrace.ui.screens.authScreens.otpVerification
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dearos.clickandrace.R
import com.dearos.clickandrace.ui.screens.authScreens.otpVerification.component.OtpInputField

/**
 * Pantalla que representa el conjunto de campos de entrada para la verificación OTP (One-Time Password).
 * Dispone de 4 campos alineados horizontalmente, cada uno manejado de forma individual para recibir un dígito numérico.
 *
 * @param state Estado actual de la pantalla, incluyendo el código OTP como lista de enteros.
 * @param focusRequesters Lista de FocusRequester, uno por cada campo, para gestionar el enfoque del teclado.
 * @param onAction Callback que permite comunicar eventos desde los campos al controlador (como cambios de número o navegación con el teclado).
 * @param modifier Modificador de Jetpack Compose para aplicar estilo o comportamiento adicional a la pantalla.
 */
@Composable
fun OtpScreen(
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estructura vertical principal
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally // Centrar horizontalmente su contenido
    ) {
        // Fila de campos OTP (normalmente 4)
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp), // Margen horizontal
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally) // Espaciado uniforme entre campos
        ) {
            // Por cada dígito en el estado, creamos un campo de entrada
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number, // Valor del dígito actual
                    focusRequester = focusRequesters[index], // Manejador de foco específico para este campo
                    onFocusChanged = { isFocused ->
                        // Si se enfoca este campo, notificamos el cambio
                        if (isFocused) {
                            onAction(OtpAction.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        // Cuando cambia el valor, se dispara esta acción
                        onAction(OtpAction.OnEnterNumber(newNumber, index))
                    },
                    onKeyboardBack = {
                        // Si el usuario pulsa retroceso y el campo está vacío
                        onAction(OtpAction.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f) // Que todos los campos ocupen el mismo ancho
                        .aspectRatio(1f) // Hacer los campos cuadrados
                )
            }
        }

        // Sección opcional (comentada) para mostrar un mensaje de validación del OTP
        state.isValid?.let { isValid ->
            Text(
                text = if (isValid) stringResource(R.string.otp_valid) else stringResource(R.string.otp_invalid),
                color = if(isValid) Color(0xff23c760) else Color.Red,
                modifier = Modifier.padding(top = 6.dp),
                fontSize = 16.sp
            )
        }
    }
}
