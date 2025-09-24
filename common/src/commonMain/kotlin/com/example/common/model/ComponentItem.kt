package com.example.common.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
data class ComponentItem(
    val id: Int,
    val text: String,
    val color: String,
    val type: String
) {
    val colorColor: Color
        get() = hexToColor(color)


    fun toMap(): Map<String, String> {
        return mapOf(
            "id" to id.toString(),
            "type" to type,
            "text" to text,
            "color" to color
        )
    }
}

fun hexToColor(hex: String): Color {
    val cleanHex = hex.removePrefix("#")
    val colorInt = cleanHex.toLong(16)
    return Color((colorInt or 0xFF000000).toInt())
}

fun Color.toHex(): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    return String.format("#%02X%02X%02X", r, g, b)
}