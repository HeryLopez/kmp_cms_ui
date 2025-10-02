package com.example.common.utils

import com.example.common.model.ButtonComponent
import com.example.common.model.ComponentItem
import com.example.common.model.ImageComponent
import com.example.common.model.LayoutNode
import com.example.common.model.TextComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object ComponentJsonMapper {

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true

        // Esta es la clave que indica el tipo de clase en tu JSON
        classDiscriminator = "type"

        serializersModule = SerializersModule {
            polymorphic(LayoutNode::class) {
                subclass(LayoutNode.Container::class, LayoutNode.Container.serializer())
                subclass(LayoutNode.Component::class, LayoutNode.Component.serializer())
            }

            polymorphic(ComponentItem::class) {
                subclass(TextComponent::class, TextComponent.serializer())
                subclass(ImageComponent::class, ImageComponent.serializer())
                subclass(ButtonComponent::class, ButtonComponent.serializer())
            }
        }
    }

    fun fromJson(jsonString: String): LayoutNode.Container {
        val root = json.decodeFromString(LayoutNode.Container.serializer(), jsonString)
        return root
    }

    fun toJson(root: LayoutNode): String {
        return json.encodeToString(LayoutNode.serializer(), root)
    }
}