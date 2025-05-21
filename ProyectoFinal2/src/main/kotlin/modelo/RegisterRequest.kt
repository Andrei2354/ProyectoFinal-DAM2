package modelo

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val nombre: String,
    val email: String,
    val direccion: String,
    val telefono: String,
    val password: String
)