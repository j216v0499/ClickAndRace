package com.dearos.clickandrace.ui.screens.authScreens.otpVerification.component
import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

/**
 * Componente composable que representa un campo de entrada de un solo dígito para la verificación OTP (One-Time Password).
 * Este campo permite ingresar solo un número (0-9), y la navegación con la tecla de retroceso.
 *
 * @param number Valor actual del dígito mostrado en el campo (puede ser null).
 * @param focusRequester Objeto usado para solicitar el foco de forma programática.
 * @param onFocusChanged Callback invocado cuando cambia el estado de foco del campo.
 * @param onNumberChanged Callback invocado cuando cambia el número introducido.
 * @param onKeyboardBack Callback invocado cuando se presiona la tecla de retroceso y el campo está vacío.
 * @param modifier Modificador de Jetpack Compose para aplicar estilo o comportamiento adicional.
 */
@Composable
fun OtpInputField(
    number: Int?,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onKeyboardBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado del texto del campo, recordado para que sobreviva
    val text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(), // Si number es null, el texto será vacío
                selection = TextRange(
                    index = if (number != null) 1 else 0 // Posición del cursor: al final si hay número, al inicio si está vacío
                )
            )
        )
    }

    // Estado que guarda si el campo tiene el foco actualmente
    var isFocused by remember {
        mutableStateOf(false)
    }

    // Contenedor visual del campo con borde y fondo dinámico según el foco
    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = if (isFocused) Color.Black else Color.LightGray, // Borde cambia según si está enfocado
                shape = MaterialTheme.shapes.medium
            )
            .background(color = Color.White, shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        // Campo de texto básico personalizado (sin estilo Material por defecto)
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                val newNumber = newText.text
                if (newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                    // Acepta máximo 1 dígito numérico
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            cursorBrush = SolidColor(Color.Black), // Color del cursor
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword // Muestra teclado numérico
            ),
            modifier = Modifier
                .padding(4.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val didPressDelete = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if (didPressDelete && number == null) {
                        // Si el campo está vacío y se presiona borrar, llama a onKeyboardBack()
                        onKeyboardBack()
                    }
                    false // No consume el evento completamente
                },
            decorationBox = { innerBox ->
                innerBox()
                if (!isFocused && number == null) {
                    // Si el campo está vacío y no tiene foco, se muestra un guion como placeholder
                    Text(
                        text = "-",
                        textAlign = TextAlign.Center,
                        color = Color.LightGray,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }
            }
        )
    }
}
