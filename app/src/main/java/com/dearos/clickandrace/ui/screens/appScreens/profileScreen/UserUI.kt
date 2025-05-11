//package com.dearos.clickandrace.ui.screens.profileScreen
//
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun UserListScreen(viewModel: UserViewModel = viewModel()) {
//    val users by viewModel.users.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadUsers()
//    }
//
//    LazyColumn {
//        items(users) { user ->
//            Text(user.name)
//        }
//    }
//}
