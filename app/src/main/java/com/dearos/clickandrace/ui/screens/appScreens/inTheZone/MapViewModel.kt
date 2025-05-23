package com.dearos.clickandrace.ui.screens.appScreens.inTheZone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.appRepository.LocationRepository
import com.dearos.clickandrace.auth.domain.appRepository.UserRepository
import com.dearos.clickandrace.model.dto.LocationDTO
import com.dearos.clickandrace.model.data.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica y estado de la pantalla de mapa.
 * Gestiona las ubicaciones, usuario actual y operaciones CRUD sobre las ubicaciones.
 */
class MapViewModel(
    private val locationRepository: LocationRepository,
    val userRepository: UserRepository
) : ViewModel() {

    // Ubicación actualmente enfocada en el mapa (para centrar la vista)
    var selectedFocus by mutableStateOf<LocationData?>(null)

    // Lista de todas las ubicaciones disponibles
    var locations by mutableStateOf<List<LocationData>>(emptyList())
        private set

    // Lista de tipos únicos de ubicaciones (por ejemplo, "parque", "restaurante")
    var locationTypes by mutableStateOf<List<String>>(emptyList())
        private set

    // ID del usuario actual, expuesto como StateFlow para observar cambios
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    // Estado que indica si la última operación (ej. agregar, eliminar) fue exitosa o no
    private val _operationSuccess = MutableStateFlow<Boolean?>(null)
    val operationSuccess = _operationSuccess.asStateFlow()

    init {
        // Carga el ID del usuario actual al inicializar el ViewModel
        loadCurrentUserId()
    }

    /**
     * Carga el ID del usuario actual desde el repositorio y actualiza el StateFlow.
     */
    fun loadCurrentUserId() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            _currentUserId.value = userId
        }
    }

    /**
     * Obtiene todas las ubicaciones desde el repositorio y actualiza la lista local.
     */
    fun getLocations() {
        viewModelScope.launch {
            try {
                locations = locationRepository.getLocations()
            } catch (e: Exception) {
                e.printStackTrace()  // Manejo básico de errores: imprimir en consola
            }
        }
    }

    /**
     * Añade una nueva ubicación si las coordenadas son válidas y el usuario está identificado.
     */
    fun addLocation(name: String, latitude: Double, longitude: Double, type: String) {
        if (!isValidCoordinate(latitude, longitude)) {
            println("ERROR: Coordenadas inválidas ($latitude, $longitude)")
            return
        }
        viewModelScope.launch {
            val id = _currentUserId.value
            if (id == null) {
                _operationSuccess.value = false  // Indica fallo por falta de usuario
                return@launch
            }
            try {
                val locationDTO = LocationDTO(name, id, latitude, longitude, type)
                locationRepository.addLocation(locationDTO)
                getLocations() // Recarga la lista tras añadir
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Actualiza una ubicación existente con nuevos datos, validando coordenadas.
     */
    fun updateLocation(
        id: String,
        userId: String,
        name: String,
        latitude: Double,
        longitude: Double,
        type: String
    ) {
        if (!isValidCoordinate(latitude, longitude)) {
            println("ERROR: Coordenadas inválidas ($latitude, $longitude)")
            return
        }
        viewModelScope.launch {
            try {
                val updatedLocation = LocationDTO(name, userId, latitude, longitude, type)
                locationRepository.updateLocation(id, updatedLocation)
                getLocations() // Refresca la lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Actualiza la ubicación enfocada en el mapa para centrar la vista.
     */
    fun focusOnLocation(location: LocationData?) {
        selectedFocus = location
    }

    /**
     * Obtiene los tipos únicos de ubicaciones para filtrado o categorización.
     */
    fun getUniqueTypes() {
        viewModelScope.launch {
            try {
                val typesFromRepo = locationRepository.getUniqueTypes()
                locationTypes = typesFromRepo // Actualiza y provoca recomposición
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Elimina una ubicación por su ID y recarga la lista.
     */
    fun deleteLocation(id: String) {
        viewModelScope.launch {
            try {
                locationRepository.deleteLocation(id)
                getLocations()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Valida si las coordenadas están dentro del rango válido para latitud y longitud.
     */
    fun isValidCoordinate(latitude: Double, longitude: Double): Boolean {
        return latitude in -85.05112878..85.05112878 && longitude in -180.0..180.0
    }
}
