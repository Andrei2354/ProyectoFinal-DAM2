package modelo

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id_producto: Int,
    val nombre: String,
    val descripcion: String,
    val id_categoria: Int,
    val id_marca: Int,
    val precio: Double,
    val imagen_url: String
)