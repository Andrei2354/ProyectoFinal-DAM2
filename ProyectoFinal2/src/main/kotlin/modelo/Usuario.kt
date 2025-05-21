package modelo

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String
)