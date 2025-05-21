package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
