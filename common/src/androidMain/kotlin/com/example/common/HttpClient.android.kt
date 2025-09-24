package com.example.common

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

actual fun httpClient(): HttpClient = io.ktor.client.HttpClient(OkHttp) {
    // configuraciones opcionales
}