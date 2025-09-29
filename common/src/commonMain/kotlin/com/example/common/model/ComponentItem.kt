package com.example.common.model

import androidx.compose.ui.graphics.Color
import com.example.common.utils.ColorUtils
import kotlinx.serialization.Serializable

@Serializable
data class ComponentItem(
    val id: Int,
    val text: String,
    val color: String,
    val type: String
) {
    val colorColor: Color
        get() = ColorUtils.hexToColor(color)


    fun toMap(): Map<String, String> {
        return mapOf(
            "id" to id.toString(),
            "type" to type,
            "text" to text,
            "color" to color
        )
    }
}
