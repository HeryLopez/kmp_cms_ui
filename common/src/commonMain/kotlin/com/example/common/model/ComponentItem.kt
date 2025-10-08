package com.example.common.model

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.common.utils.ColorUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class NodeType(val type: String) {
    CONTAINER("container_component"),
    TEXT_BLOCK("text_component"),
    IMAGE_BLOCK("image_component"),
    BUTTON_BLOCK("button_component");

    companion object {
        fun fromType(type: String): NodeType? {
            return entries.firstOrNull { it.type == type }
        }
    }
}

enum class ComponentType(val type: String, val title: String) {
    TEXT_BLOCK("text_component", "Text Block"),
    IMAGE_BLOCK("image_component", "Image Block"),
    BUTTON_BLOCK("button_component", "Button Block");

    companion object {
        fun fromType(type: String): ComponentType? {
            return ComponentType.entries.firstOrNull { it.type == type }
        }
    }
}

@Serializable
sealed class LayoutNode {
    @Serializable
    @SerialName("container")
    data class Container(
        val id: String = generateId(),

        // Content orientation
        val orientation: Orientation = Orientation.Column,

        // Size (null = use available space)
        val width: Float? = null,      // en dp
        val height: Float? = null,     // en dp

        // Padding
        val padding: Float = 0f,
        val contentPadding: Float = 0f,

        // Background and border
        val backgroundColor: String? = null,
        val borderColor: String? = null,
        val borderWidth: Float = 0f,

        // Round Corner
        val topStartRadius: Float = 0f,
        val topEndRadius: Float = 0f,
        val bottomStartRadius: Float = 0f,
        val bottomEndRadius: Float = 0f,

        // Shadows
        val elevation: Float = 0f,

        // Items Spacing
        val spacing: Float = 0f,

        // FlowRow
        val flowMinCellSize: Float? = null,
        val flowColumns: Int? = null,

        val children: List<LayoutNode> = listOf()
    ) : LayoutNode()

    @Serializable
    @SerialName("component")
    data class Component(
        val id: String = generateId(),
        val component: ComponentItem
    ) : LayoutNode()

    val nodeId: String
        get() = when (this) {
            is Container -> this.id
            is Component -> this.id
        }

    companion object {
        fun generateId() = java.util.UUID.randomUUID().toString()
    }
}

enum class Orientation { Row, Column, Grid }


@Serializable
sealed interface ComponentItem {
    val id: String

    @SerialName("componentType")
    val type: String
}

@Serializable
@SerialName("text_component")
data class TextComponent(
    override val id: String,
    @SerialName("componentType")
    override val type: String = ComponentType.TEXT_BLOCK.type,
    val text: String,
    val textColor: String? = null,
    val backgroundColor: String? = null,
    val fontSize: Float? = null,
    val fontWeight: String? = null,
    val fontStyle: String? = null,
    val textAlign: String? = null
) : ComponentItem {
    val textColorValue: Color?
        get() = ColorUtils.hexToColor(textColor)

    val backgroundColorValue: Color?
        get() = ColorUtils.hexToColor(backgroundColor)


    val fontWeightValue: FontWeight
        get() = when (fontWeight?.lowercase()) {
            "bold" -> FontWeight.Bold
            "medium" -> FontWeight.Medium
            "light" -> FontWeight.Light
            "thin" -> FontWeight.Thin
            "black" -> FontWeight.Black
            else -> FontWeight.Normal
        }

    val fontStyleValue: FontStyle
        get() = when (fontStyle?.lowercase()) {
            "italic" -> FontStyle.Italic
            else -> FontStyle.Normal
        }

    val fontSizeValue: TextUnit
        get() = fontSize?.sp ?: 14.sp

    val textAlignValue: TextAlign
        get() = when (textAlign?.lowercase()) {
            "start" -> TextAlign.Start
            "center" -> TextAlign.Center
            "end" -> TextAlign.End
            else -> TextAlign.Start
        }

    val containerAlignment: Alignment
        get() = when (textAlign?.lowercase()) {
            "start" -> Alignment.CenterStart
            "center" -> Alignment.Center
            "end" -> Alignment.CenterEnd
            else -> Alignment.CenterStart
        }
}

@Serializable
@SerialName("image_component")
data class ImageComponent(
    override val id: String,
    @SerialName("componentType")
    override val type: String = ComponentType.IMAGE_BLOCK.type,
    val title: String,
    val backgroundImageUrl: String,
    val titleColor: String? = null,
    // Corner Shape
    val topStartRadius: Float = 0f,
    val topEndRadius: Float = 0f,
    val bottomStartRadius: Float = 0f,
    val bottomEndRadius: Float = 0f,
    // Dimensions
    val width: Float? = null,
    val height: Float? = null
) : ComponentItem {
    val titleColorValue: Color?
        get() = ColorUtils.hexToColor(titleColor)
}

@Serializable
@SerialName("button_component")
data class ButtonComponent(
    override val id: String,
    @SerialName("componentType")
    override val type: String = ComponentType.BUTTON_BLOCK.type,
    val text: String,
    val actionType: String,
    val actionValue: String
) : ComponentItem
