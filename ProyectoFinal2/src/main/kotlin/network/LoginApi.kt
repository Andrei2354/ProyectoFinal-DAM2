package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.LoginRequest
import modelo.Usuario
import network.NetworkUntils.httpClient
import utils.sha512

fun apiLogin(usuario: String, password: String, onSuccessResponse: (Usuario) -> Unit){
    val url = "http://127.0.0.1:5000/login_user"
    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.post(url){
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(usuario, password))
        }
        if (response.status == HttpStatusCode.OK){
            val user = response.body<Usuario>()
            onSuccessResponse(user)
        } else{
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}

