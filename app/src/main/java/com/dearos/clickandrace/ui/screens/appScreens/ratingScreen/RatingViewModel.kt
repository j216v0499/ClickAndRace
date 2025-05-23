package com.dearos.clickandrace.ui.screens.appScreens.ratingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.model.dto.RatingDTO
import com.dearos.clickandrace.auth.domain.appRepository.RatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica de la pantalla de reseñas (ratings).
 *
 * Provee funciones para cargar reseñas de un usuario,
 * enviar una nueva reseña y verificar si un usuario ya ha valorado a otro.
 *
 * @property ratingRepository Repositorio para manejar la persistencia de ratings.
 */
class RatingViewModel(
    private val ratingRepository: RatingRepository
) : ViewModel() {

    /** Indica si la última operación (crear rating) fue exitosa */
    private val _operationSuccess = MutableStateFlow(false)
    val operationSuccess: StateFlow<Boolean> = _operationSuccess

    /** Estado de carga durante operaciones de red o base de datos */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /** Indica si el usuario ya ha valorado al usuario objetivo */
    private val _hasRated = MutableStateFlow(false)
    val hasRated: StateFlow<Boolean> = _hasRated

    /** Lista de reseñas recibidas por un usuario */
    private val _userRatings = MutableStateFlow<List<RatingDTO>>(emptyList())
    val userRatings: StateFlow<List<RatingDTO>> = _userRatings

    /**
     * Carga las reseñas recibidas por un usuario específico.
     *
     * @param userId ID del usuario cuyas reseñas se quieren obtener.
     */
    fun loadRatingsForUser(userId: String) {
        viewModelScope.launch {
            _userRatings.value = ratingRepository.getRatingsForRatedUser(userId)
        }
    }

    /**
     * Envía una nueva reseña para un usuario.
     *
     * Actualiza estados de carga y éxito según resultado.
     *
     * @param ratingDTO Objeto con los datos de la reseña.
     */
    fun rateUser(ratingDTO: RatingDTO) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = ratingRepository.rateUser(ratingDTO)
            _operationSuccess.value = success
            _isLoading.value = false
        }
    }

    /**
     * Verifica localmente si un usuario ya ha valorado a otro.
     *
     * Esta función consulta todas las reseñas y actualiza [_hasRated]
     * si existe alguna reseña entre el usuario que valora y el usuario valorado.
     *
     * @param raterId ID del usuario que realiza la valoración.
     * @param ratedUserId ID del usuario que recibe la valoración.
     */
    fun checkIfRatedLocally(raterId: String, ratedUserId: String) {
        viewModelScope.launch {
            val allRatings = ratingRepository.getAllRatings()
            _hasRated.value = allRatings.any {
                it.rater_id == raterId && it.user_id == ratedUserId
            }
        }
    }
}
