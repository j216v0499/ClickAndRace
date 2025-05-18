package com.dearos.clickandrace.model.data

import kotlinx.serialization.Serializable

/**
 * Representa un usuario dentro de la aplicación.
 *
 * @property id Identificador del usuario.
 * @property email Correo electrónico del usuario.
 * @property name Nombre del usuario.
 * @property phone Número de teléfono (opcional).
 * @property profile_picture URL de la foto de perfil (opcional).
 * @property rating Valor promedio de las valoraciones.
 * @property createdAt Fecha de creación de la cuenta (opcional).
 */
@Serializable
data class UserData(
    val id: String,
    val email: String,
    val name: String,
    val phone: String? = null,
    val profile_picture: String? = null,
    val rating: Float = 0f,
    val createdAt: String? = null
)
