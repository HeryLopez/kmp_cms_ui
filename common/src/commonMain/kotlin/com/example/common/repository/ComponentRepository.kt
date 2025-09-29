package com.example.common.repository

import com.example.common.httpClient
import com.example.common.model.ComponentItem
import com.example.common.utils.ComponentJsonMapper
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray


class ComponentRepository(private val baseUrl: String = "http://10.0.2.2:9090") {

    private val client = httpClient()

    suspend fun getJsonItems(): List<String> {
        val json: String = client.get("$baseUrl/jsonlist").body()
        val jsonArray = Json.parseToJsonElement(json).jsonArray

        val itemStrings = jsonArray.map { it.toString() }
        return itemStrings
    }

    suspend fun getAll(): List<ComponentItem> {
        val json: String = client.get("$baseUrl/jsonlist").body()
        val jsonArray = Json.parseToJsonElement(json).jsonArray

        val itemStrings = jsonArray.map { it.toString() }
        return ComponentJsonMapper.fromJson(itemStrings)
    }

    suspend fun saveJson(itemStrings: List<String>): Boolean {
        val jsonArray = itemStrings.joinToString(prefix = "[", postfix = "]", separator = ",")

        val response: HttpResponse = client.post("$baseUrl/jsonlist") {
            contentType(ContentType.Application.Json)
            setBody(jsonArray)
        }
        return response.status == HttpStatusCode.OK
    }
}