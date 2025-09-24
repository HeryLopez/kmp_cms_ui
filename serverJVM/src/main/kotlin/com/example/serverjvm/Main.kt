package com.example.serverjvm

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray

fun main() {
    initDatabase()

    embeddedServer(Netty, port = 9090) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            get("/jsonlist") {
                val itemsJson: List<String> = fetchAllItems()
                call.respond(buildJsonArray {
                    itemsJson.forEach { add(Json.parseToJsonElement(it)) }
                })
            }

            post("/jsonlist") {
                val json = call.receive<String>()
                insertItem(json)
                call.respondText("Item added successfully")
            }
        }
    }.start(wait = true)
}
