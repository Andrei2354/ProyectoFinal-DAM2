package modelo

import kotlinx.serialization.Serializable

@Serializable
data class Marca(
    val id: Int,
    val nombre: String
)