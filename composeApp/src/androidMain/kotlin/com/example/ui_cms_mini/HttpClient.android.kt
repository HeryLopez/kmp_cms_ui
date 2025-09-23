package com.example.ui_cms_mini

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

actual fun httpClient(): HttpClient = HttpClient(OkHttp) {
    // configuraciones opcionales
}