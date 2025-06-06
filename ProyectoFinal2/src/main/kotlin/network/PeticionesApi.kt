package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import modelo.*
import network.NetworkUntils.httpClient

fun apiEditProfile(
    userId: Int,
    nombre: String,
    email: String,
    direccion: String,
    telefono: String,
    onComplete: (Boolean, Usuario?) -> Unit
) {
    val url = "http://127.0.0.1:5000/edit_profile"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "id" to userId.toString(),
                        "nombre" to nombre,
                        "email" to email,
                        "direccion" to direccion,
                        "telefono" to telefono
                    )
                )
            }

            val success = response.status.isSuccess()
            val updatedUser = if (success) {
                Usuario(
                    id = userId,
                    nombre = nombre,
                    email = email,
                    direccion = direccion,
                    telefono = telefono
                )
            } else null

            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success, updatedUser)
            }

            println("Edit profile response: ${response.status}, ${response.bodyAsText()}")
        } catch (e: Exception) {
            println("Edit profile error: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false, null)
            }
        }
    }
}

fun apiRegister(
    nombre: String,
    email: String,
    direccion: String,
    telefono: String,
    password: String,
    onComplete: (Boolean) -> Unit
) {
    val url = "http://127.0.0.1:5000/register_user"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(nombre, email, direccion, telefono, password))
            }

            val success = response.status.isSuccess()
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success)
            }

            println("Register response: ${response.status}, ${response.bodyAsText()}")
        } catch (e: Exception) {
            println("Register error: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false)
            }
        }
    }
}

fun apiGetProductos(onComplete: (List<Producto>) -> Unit) {
    val url = "http://127.0.0.1:5000/productos"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)

            if (response.status.isSuccess()) {
                val productos = response.body<List<Producto>>()
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(productos)
                }
            } else {
                println("Error al obtener productos: ${response.status}")
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(emptyList())
                }
            }
        } catch (e: Exception) {
            println("Error en la petición de productos: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(emptyList())
            }
        }
    }
}

fun apiGetCategorias(onComplete: (List<Categoria>) -> Unit) {
    val url = "http://127.0.0.1:5000/categorias"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)

            if (response.status.isSuccess()) {
                val categorias = response.body<List<Categoria>>()
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(categorias)
                }
            } else {
                println("Error al obtener categorías: ${response.status}")
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(emptyList())
                }
            }
        } catch (e: Exception) {
            println("Error en la petición de categorías: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(emptyList())
            }
        }
    }
}

fun apiGetMarcas(onComplete: (List<Marca>) -> Unit) {
    val url = "http://127.0.0.1:5000/marcas"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)

            if (response.status.isSuccess()) {
                val marcas = response.body<List<Marca>>()
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(marcas)
                }
            } else {
                println("Error al obtener marcas: ${response.status}")
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(emptyList())
                }
            }
        } catch (e: Exception) {
            println("Error en la petición de marcas: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(emptyList())
            }
        }
    }
}
private val json = Json { ignoreUnknownKeys = true }

fun apiAgregarAlCarrito(
    usuarioId: Int,
    productoId: Int,
    cantidad: Int,
    precioUnitario: Double,
    onComplete: (Boolean, String) -> Unit
) {
    val url = "http://127.0.0.1:5000/carrito/agregar"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(CarritoRequest(usuarioId, productoId, cantidad, precioUnitario))
            }

            val responseBody = response.bodyAsText()
            val success = response.status.isSuccess()

            CoroutineScope(Dispatchers.Main).launch {
                if (success) {
                    onComplete(true, "Producto agregado al carrito")
                } else {
                    onComplete(false, "Error al agregar producto")
                }
            }

            println("Agregar carrito response: ${response.status}, $responseBody")
        } catch (e: Exception) {
            println("Agregar carrito error: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false, "Error de conexión")
            }
        }
    }
}

fun apiObtenerCarrito(
    usuarioId: Int,
    onComplete: (List<ItemCarrito>) -> Unit
) {
    val url = "http://127.0.0.1:5000/carrito/$usuarioId"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)

            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                val carritoResponse = json.decodeFromString<CarritoResponse>(responseBody)

                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(carritoResponse.carrito)
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(emptyList())
                }
            }

            println("Obtener carrito response: ${response.status}")
        } catch (e: Exception) {
            println("Obtener carrito error: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(emptyList())
            }
        }
    }
}

fun apiActualizarCantidad(
    idCarrito: Int,
    nuevaCantidad: Int,
    onComplete: (Boolean) -> Unit
) {
    val url = "http://127.0.0.1:5000/carrito/actualizar"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.put(url) {
                contentType(ContentType.Application.Json)
                setBody(ActualizarCantidadRequest(idCarrito, nuevaCantidad))
            }

            val success = response.status.isSuccess()
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success)
            }

            println("Actualizar cantidad response: ${response.status}")
        } catch (e: Exception) {
            println("Actualizar cantidad error: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false)
            }
        }
    }
}

fun apiEliminarDelCarrito(
    idCarrito: Int,
    onComplete: (Boolean) -> Unit
) {
    val url = "http://127.0.0.1:5000/carrito/eliminar"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.delete(url) {
                contentType(ContentType.Application.Json)
                setBody(EliminarItemRequest(idCarrito))
            }

            val success = response.status.isSuccess()
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success)
            }

            println("Eliminar del carrito response: ${response.status}")
        } catch (e: Exception) {
            println("Eliminar del carrito error: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false)
            }
        }
    }
}
fun apiGetTodosUsuarios(onComplete: (List<UsuarioCompleto>) -> Unit) {
    val url = "http://127.0.0.1:5000/admin/usuarios"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.get(url)
            if (response.status.isSuccess()) {
                val usuarios = response.body<List<UsuarioCompleto>>()
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(usuarios)
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(emptyList())
                }
            }
        } catch (e: Exception) {
            println("Error al obtener usuarios: ${e.message}")
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(emptyList())
            }
        }
    }
}

fun apiEditarUsuario(
    usuario: UsuarioCompleto,
    onComplete: (Boolean, String) -> Unit
) {
    val url = "http://127.0.0.1:5000/admin/editar_usuario"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.put(url) {
                contentType(ContentType.Application.Json)
                setBody(usuario)
            }

            val success = response.status.isSuccess()
            val message = if (success) "Usuario actualizado correctamente" else "Error al actualizar usuario"

            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success, message)
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false, "Error de conexión")
            }
        }
    }
}

fun apiEliminarUsuario(
    usuarioId: Int,
    onComplete: (Boolean, String) -> Unit
) {
    val url = "http://127.0.0.1:5000/admin/eliminar_usuario/$usuarioId"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.delete(url)

            val success = response.status.isSuccess()
            val message = if (success) "Usuario eliminado correctamente" else "Error al eliminar usuario"

            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success, message)
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false, "Error de conexión")
            }
        }
    }
}

fun apiEditarProducto(
    producto: ProductoCompleto,
    onComplete: (Boolean, String) -> Unit
) {
    val url = "http://127.0.0.1:5000/admin/editar_producto"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.put(url) {
                contentType(ContentType.Application.Json)
                setBody(producto)
            }

            val success = response.status.isSuccess()
            val message = if (success) "Producto actualizado correctamente" else "Error al actualizar producto"

            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success, message)
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false, "Error de conexión")
            }
        }
    }
}

fun apiEliminarProducto(
    productoId: Int,
    onComplete: (Boolean, String) -> Unit
) {
    val url = "http://127.0.0.1:5000/admin/eliminar_producto/$productoId"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.delete(url)

            val success = response.status.isSuccess()
            val message = if (success) "Producto eliminado correctamente" else "Error al eliminar producto"

            CoroutineScope(Dispatchers.Main).launch {
                onComplete(success, message)
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                onComplete(false, "Error de conexión")
            }
        }
    }
}