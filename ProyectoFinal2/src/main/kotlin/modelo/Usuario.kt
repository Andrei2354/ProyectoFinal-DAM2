package modelo

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val direccion: String = "",
    val telefono: String = ""
)