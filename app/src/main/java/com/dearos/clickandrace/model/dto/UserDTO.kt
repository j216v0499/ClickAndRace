package com.dearos.clickandrace.model.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object que representa un usuario.
 * Se utiliza para enviar/recibir datos del usuario a través de la red (API).
 *
 * @param name Nombre del usuario (obligatorio).
 * @param phone Número de teléfono del usuario (opcional).
 * @param profile_picture URL o path de la imagen de perfil (opcional).
 * @param rating Valoración promedio del usuario, por defecto 0.0f (opcional).
 */
@Serializable
data class UserDTO(
    val name: String,
    val phone: String? = null,
    val profile_picture: String? = null,
    val rating: Float? = 0.0f
)
