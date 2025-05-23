package modelo

import kotlinx.serialization.Serializable

@Serializable
data class CarritoResponse(
    val success: Boolean,
    val message: String,
    val carrito: List<ItemCarrito>
)