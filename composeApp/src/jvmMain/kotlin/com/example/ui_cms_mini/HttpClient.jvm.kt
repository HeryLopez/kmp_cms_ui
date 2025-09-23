package com.example.ui_cms_mini

import io.ktor.client.*
import io.ktor.client.engine.cio.*

actual fun httpClient(): HttpClient = HttpClient(CIO) {
    // configuraciones opcionales
}
