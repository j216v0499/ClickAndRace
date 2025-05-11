//package com.dearos.clickandrace.ui.screens.profileScreen
///*
//class UserViewModel {
//}*/
//
//// presentation/viewmodel/UserViewModel.kt
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.dearos.clickandrace.data.model.UserDto
//import com.dearos.clickandrace.data.repository.UserRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class UserViewModel : ViewModel() {
//
//    private val repository = UserRepository()
//
//    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
//    val users: StateFlow<List<UserDto>> = _users
//
//    fun loadUsers() {
//        viewModelScope.launch {
//            _users.value = repository.getUsers()
//        }
//    }
//
//    fun addUser(user: UserDto) {
//        viewModelScope.launch {
//            repository.createUser(user)
//            loadUsers()
//        }
//    }
//
//    fun updateUser(user: UserDto) {
//        viewModelScope.launch {
//            repository.updateUser(user)
//            loadUsers()
//        }
//    }
//
//    fun deleteUser(id: String) {
//        viewModelScope.launch {
//            repository.deleteUser(id)
//            loadUsers()
//        }
//    }
//}
