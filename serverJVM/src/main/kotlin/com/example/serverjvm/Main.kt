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

    val item1 = ComponentItem("🌟 Hello World!", "#6DE98C" , "component_item")
    val item2 = ComponentItem("🚀 Keep Smiling!", "#C99DA4", "component_item")

        // insertItem(Json.encodeToString(ComponentItem.serializer(), item1))
    //insertItem(Json.encodeToString(ComponentItem.serializer(), item2))


    embeddedServer(Netty, port = 9090) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            // GET /items -> devuelve todos los items
            get("/jsonlist") {
                val itemsJson: List<String> = fetchAllItems()
                call.respond(buildJsonArray {
                    itemsJson.forEach { add(Json.parseToJsonElement(it)) }
                })
            }

            // POST /items -> agrega un JSON
            post("/jsonlist") {
                val json = call.receive<String>()   // recibimos JSON crudo
                insertItem(json)
                call.respondText("Item added successfully")
            }
        }
    }.start(wait = true)
}
