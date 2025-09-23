package com.example.ui_cms_mini

interface Platform {
    val name: String
    val platform: String
}

expect fun getPlatform(): Platform