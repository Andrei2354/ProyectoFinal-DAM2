package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.EditProfileRequest
import modelo.LoginRequest
import network.NetworkUntils.httpClient
import modelo.RegisterRequest
import modelo.Usuario

fun apiEditProfile(
    userId: Int,
    nombre: String,
    email: String,
    direccion: String,
    telefono: String,
    onComplete: (Boolean, modelo.Usuario?) -> Unit
) {
    val url = "http://127.0.0.1:5000/edit_profile"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "id" to userId.toString(), // Convertir el userId a String
                        "nombre" to nombre,
                        "email" to email,
                        "direccion" to direccion,
                        "telefono" to telefono
                    )
                )
            }

            val success = response.status.isSuccess()
            val updatedUser = if (success) {
                modelo.Usuario(
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