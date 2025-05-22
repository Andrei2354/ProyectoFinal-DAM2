package modelo

import kotlinx.serialization.Serializable

@Serializable
data class ItemCarrito(
    val id_carrito: Int = 0,
    val id_usuario: Int,
    val id_producto: Int,
    val cantidad: Int,
    val precio_unitario: Double,
    val fecha_agregado: String = "",
    val nombre_producto: String = "",
    val imagen_url: String = "",
    val descripcion: String = ""
)

@Serializable
data class CarritoRequest(
    val id_usuario: Int,
    val id_producto: Int,
    val cantidad: Int,
    val precio_unitario: Double
)

@Serializable
data class ActualizarCantidadRequest(
    val id_carrito: Int,
    val cantidad: Int
)

@Serializable
data class EliminarItemRequest(
    val id_carrito: Int
)

@Serializable
data class CarritoResponse(
    val success: Boolean,
    val message: String,
    val carrito: List<ItemCarrito> = emptyList()
)