//package com.dearos.clickandrace.data.repository
//
//import com.dearos.clickandrace.auth.domain.supabase.SupabaseClientProvider
//import com.dearos.clickandrace.data.model.UserDto
//import io.github.jan.supabase.postgrest.from
//import io.github.jan.supabase.postgrest.query.filter
//
//class UserRepository {
//
//    private val client = SupabaseClientProvider.client
//
//    // Obtener todos los usuarios
//    suspend fun getUsers(): List<UserDto> {
//        return client.from("users")
//            .select()
//            .decodeList<UserDto>()  // Decodifica la lista de usuarios
//    }
//
//    // Obtener un usuario por ID
//    suspend fun getUserById(id: String): UserDto? {
//        return client.from("users")
//            .select()
//            .filter("id", "eq", id)  // Filtra por id
//            .decodeSingleOrNull<UserDto>()  // Decodifica un único usuario
//    }
//
//    // Crear un nuevo usuario
//    suspend fun createUser(user: UserDto) {
//        client.from("users")
//            .insert(user)  // Inserta un nuevo usuario
//    }
//
//    // Actualizar un usuario existente
//    suspend fun updateUser(user: UserDto) {
//        client.from("users")
//            .update(user)
//            .filter("id", "eq", user.id)  // Filtra por el ID del usuario
//    }
//
//    // Eliminar un usuario por ID
//    suspend fun deleteUser(id: String) {
//        client.from("users")
//            .delete()
//            .filter("id", "eq", id)  // Filtra por id para eliminar el usuario correspondiente
//    }
//}
