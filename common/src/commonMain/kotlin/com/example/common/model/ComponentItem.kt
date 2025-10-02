package com.example.common.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
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
        val orientation: Orientation = Orientation.Column,
        val padding: Float = 0f,
        val backgroundColor: String = "#FFFFFF" ,
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

enum class Orientation { Row, Column }


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
    val color: String
) : ComponentItem {
    val colorColor: Color
        get() = ColorUtils.hexToColor(color)
}

@Serializable
@SerialName("image_component")
data class ImageComponent(
    override val id: String,
    @SerialName("componentType")
    override val type: String = ComponentType.IMAGE_BLOCK.type,
    val title: String,
    val backgroundImageUrl: String,
    val titleColor: String
) : ComponentItem {
    val titleColorColor: Color
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
