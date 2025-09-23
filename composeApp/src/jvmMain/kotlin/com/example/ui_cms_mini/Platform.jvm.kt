package com.example.ui_cms_mini

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val platform: String = "desktop"
}

actual fun getPlatform(): Platform = JVMPlatform()