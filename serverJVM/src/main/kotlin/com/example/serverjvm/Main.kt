package com.example.serverjvm

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    initDatabase()

    embeddedServer(Netty, port = 9090) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            get("/jsonlist") {
                val json: String? = fetchItem()
                call.respond(json ?: "")
            }

            post("/jsonlist") {
                val json = call.receive<String>()
                saveItem(json)
                call.respondText("Item added successfully")
            }
        }
    }.start(wait = true)
}
