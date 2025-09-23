package com.example.ui_cms_mini.repository

import com.example.ui_cms_mini.httpClient
import com.example.ui_cms_mini.model.ComponentItem
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray


class ComponentRepository(private val baseUrl: String = "http://10.0.2.2:9090") {

    // Usa la función multiplataforma
    private val client = httpClient()

    // Obtener todos los items
    suspend fun getAll(): List<String> {
        val response: String = client.get("$baseUrl/jsonlist").body()
        // parsea el JSON array recibido a lista de strings (cada elemento es un JSON)
        val jsonArray = Json.parseToJsonElement(response).jsonArray
        return jsonArray.map { it.toString() }
    }

    // Guardar un item
    suspend fun save(item: ComponentItem): Boolean {
        val response: HttpResponse = client.post("$baseUrl/jsonlist") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(ComponentItem.serializer(), item))
        }
        return response.status == HttpStatusCode.OK
    }
}