package com.dearos.clickandrace.data.model
/*
class UserDto {
}
*/

//// data/model/UserDto.kt
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class User(
//    val id: String,
//    val name: String,
//    val email: String
//)


data class User(
    val id: String,                                     // UUID del usuario
    val name: String,                                   // Nombre del usuario
    val email: String,                                  // Correo electrónico
    val phone: String? = null,                          // Número de teléfono (opcional)
    val rating: Float = 0f,                             // Valoración promedio
    val profilePicture: String? = null,                 // URL de la foto de perfil (opcional)
    val defaultLocation: String? = null,                // Ubicación predeterminada
    val defaultAvailability: Map<String, Any>? = null,   // Horario predeterminado
    val createdAt: String?
)