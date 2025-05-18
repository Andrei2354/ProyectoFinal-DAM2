package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.NetworkUntils.httpClient


/*
fun MostrarProyectos(onSuccessResponse: (List<Proyecto>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/proyectos"
    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listProyecto = response.body<List<Proyecto>>()
            onSuccessResponse(listProyecto)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}
fun MostrarTodosProyectos(onSuccessResponse: (List<Proyecto>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/proyectos_activos"

    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listProyecto = response.body<List<Proyecto>>()
            onSuccessResponse(listProyecto)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}


fun MostrarMisProyectos(id: Int, onSuccessResponse: (List<Proyecto>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/proyectos_gestor?id=$id"

    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listProyecto = response.body<List<Proyecto>>()
            onSuccessResponse(listProyecto)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}

fun MostrarMistareas(id: Int, onSuccessResponse: (List<Tarea>) -> Unit) {
    val url = "http://127.0.0.1:5000/tareas/proyectos?id=$id"

    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listTarea = response.body<List<Tarea>>()
            onSuccessResponse(listTarea)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}
*/