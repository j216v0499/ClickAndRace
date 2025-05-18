package com.dearos.clickandrace.ui.screens.appScreens.homeScreen

/**
 * Representa el estado de la interfaz de usuario para la HomeScreen.
 *
 * @property userName Nombre del usuario que ha iniciado sesión (por defecto, "Guest").
 * @property accessToken Token de acceso actual.
 * @property refreshToken Token de actualización actual. Útil para renovar sesiones.
 * @property errorMessage Mensaje de error a mostrar en caso de fallo.
 * @property isLoading Indica si la UI está esperando datos o completando una operación.
 */
// UI state data class for the Home Screen
data class HomeUiState(
    val userName: String = "Guest",
    val accessToken: String = "No Access Token",
    val refreshToken: String = "No Refresh Token",
    val errorMessage: String? = null,
    val isLoading: Boolean = true
)

