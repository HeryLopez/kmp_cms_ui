package com.example.common.repository

import com.example.common.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ComponentRepository(private val baseUrl: String = "http://10.0.2.2:9090") {
    private val client = httpClient()

    suspend fun getJson(): String {
        val json: String = client.get("$baseUrl/jsonlist").body()
        return json
    }

    suspend fun saveJson(json: String): Boolean {
        val response: HttpResponse = client.post("$baseUrl/jsonlist") {
            contentType(ContentType.Application.Json)
            setBody(json)
        }
        return response.status == HttpStatusCode.OK
    }
}