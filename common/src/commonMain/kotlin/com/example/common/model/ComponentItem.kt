package com.example.common.model

import androidx.compose.ui.graphics.Color
import com.example.common.utils.ColorUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class ComponentType(val type: String, val title: String){
    TEXT_BLOCK("text_component", "Text Block"),
    IMAGE_BLOCK("image_component", "Image Block")
}

@Serializable
sealed interface ComponentItem {
    val id: Int
    val type: String
    fun toMap(): Map<String, String>
}

@Serializable
@SerialName("text_component")
data class TextComponent(
    override val id: Int,
    override val type: String = ComponentType.TEXT_BLOCK.type,
    val text: String,
    val color: String
) : ComponentItem {
    val colorColor: Color
        get() = ColorUtils.hexToColor(color)


    override fun toMap(): Map<String, String> {
        return mapOf(
            "id" to id.toString(),
            "type" to type,
            "text" to text,
            "color" to color
        )
    }
}

@Serializable
@SerialName("image_component")
data class ImageComponent(
    override val id: Int,
    override val type: String = ComponentType.IMAGE_BLOCK.type,
    val title: String,
    val backgroundImageUrl: String,
    val titleColor: String
) : ComponentItem {
    val titleColorColor: Color
        get() = ColorUtils.hexToColor(titleColor)

    override fun toMap(): Map<String, String> {
        return mapOf(
            "id" to id.toString(),
            "type" to type,
            "title" to title,
            "backgroundImageUrl" to backgroundImageUrl,
            "titleColor" to titleColor
        )
    }
}

