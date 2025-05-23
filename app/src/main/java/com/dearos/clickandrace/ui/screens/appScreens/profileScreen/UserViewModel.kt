package com.dearos.clickandrace.ui.screens.appScreens.profileScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.appRepository.UserRepository
import com.dearos.clickandrace.auth.domain.authRepository.LoginRepo

import com.dearos.clickandrace.model.dto.UserDTO
import com.dearos.clickandrace.model.data.UserData
import com.dearos.clickandrace.ui.screens.appScreens.homeScreen.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val loginRepository: LoginRepo,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState



    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            userId?.let {
                _userData.value = userRepository.getUserData(it)
            }
        }
    }

    fun loadUser(userId: String) {
        viewModelScope.launch {
            _userData.value = userRepository.getUserData(userId)
        }
    }

    fun updateUser(userId: String, name: String, phone: String?, profile_picture: String?,rating: Float?) {
        viewModelScope.launch {
            val updated = UserDTO(name, phone, profile_picture, rating )
            userRepository.updateUser(userId, updated)
            loadUser(userId) // recarga datos actualizados
        }
    }

    suspend fun uploadUserProfilePicture(uri: Uri, context: Context): String? {
        return userRepository.uploadImageToSupabase(uri, context)
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = loginRepository.logoutUser()
            if (result.isSuccess) {
                onLogoutSuccess()
            } else {
                _uiState.value = _uiState.value.copy(errorMessage = "Logout failed")
            }
        }
    }
}
