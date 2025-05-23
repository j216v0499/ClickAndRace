package com.dearos.clickandrace.ui.screens.authScreens.otpVerification

import com.dearos.clickandrace.LogsLogger


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.authRepository.SignUpRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsable de manejar la lógica de presentación para la verificación de OTP.
 * Utiliza un `StateFlow` para exponer el estado de la pantalla y procesar las acciones del usuario.
 *
 * @param repository Repositorio para realizar la verificación de email con el OTP.
 */
class OtpViewModel(
    private val repository: SignUpRepo
) : ViewModel() {

    // Estado interno mutable del OTP, inicializado con valores por defecto
    private val _state = MutableStateFlow(OtpState())

    // Estado inmutable expuesto a la UI
    val state = _state.asStateFlow()

    /**
     * Verifica el OTP introducido llamando a un repositorio remoto.
     *
     * @param email Email del usuario para verificar.
     */
    fun verifyOtp(email: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                _state.update { it.copy(isValid = false) }
                return@launch
            }

            try {
                // Convierte la lista de dígitos a un único string
                val otpCode = state.value.code.joinToString("") { it?.toString() ?: "" }

                // Llama al repositorio para verificar el email
                val result = repository.verifyEmail(email, otpCode)
                LogsLogger.d("OtpViewModel", "Backend Result: $result")

                // Actualiza el estado según el resultado
                _state.update { it.copy(isValid = result.isSuccess) }
            } catch (e: Exception) {
                LogsLogger.d("OtpViewModel", "Backend Result: ${e.message}")
                _state.update { it.copy(isValid = false) }
            }
        }
    }

    /**
     * Procesa las acciones del usuario enviadas desde la UI.
     *
     * @param action Acción que representa un evento en la UI (como escribir un número o moverse entre campos).
     */
    fun onAction(action: OtpAction) {
        when (action) {
            is OtpAction.OnChangeFieldFocused -> {
                _state.update { it.copy(focusedIndex = action.index) }
            }

            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }

            OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if (index == previousIndex) null else number
                        },
                        focusedIndex = previousIndex
                    )
                }
            }
        }
    }

    /**
     * Introduce o actualiza un número en una posición específica del código OTP.
     *
     * @param number Número introducido (o null si fue borrado).
     * @param index Índice del campo modificado.
     */
    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) number else currentNumber
        }

        val wasNumberRemoved = number == null

        _state.update {
            it.copy(
                code = newCode,
                focusedIndex = if (wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusedIndex
                } else {
                    getNextFocusedTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusedIndex
                    )
                },
                isValid = null // Se reinicia la validez tras cualquier cambio
            )
        }
    }

    /**
     * Calcula el índice anterior al actualmente enfocado.
     *
     * @param currentIndex Índice actual.
     * @return Índice anterior o el mismo si ya está al inicio.
     */
    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    /**
     * Determina el siguiente índice que debería enfocar el usuario.
     *
     * @param currentCode Lista actual de números OTP.
     * @param currentFocusedIndex Índice actual enfocado.
     * @return El siguiente índice vacío o null si no hay más.
     */
    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null || currentFocusedIndex >= currentCode.size - 1) {
            return null
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    /**
     * Busca el primer campo vacío después del índice enfocado actual.
     *
     * @param code Código OTP actual.
     * @param currentFocusedIndex Índice actual enfocado.
     * @return Índice del siguiente campo vacío o el mismo si no hay más vacíos.
     */
    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if (index > currentFocusedIndex && number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }
}
