package com.example.common.utils

import com.example.common.model.ComponentItem
import kotlinx.serialization.json.Json

object ComponentJsonMapper {

    private val json = Json {
        ignoreUnknownKeys = true // ignora campos extra
        classDiscriminator = "type" // usa el campo "type" para decidir el subtipo
    }

    /**
     * Convierte una lista de JSON strings a una lista de ComponentItem usando deserialización polimórfica.
     */
    fun fromJson(jsonList: List<String>): List<ComponentItem> {
        return jsonList.mapNotNull { jsonString ->
            try {
                json.decodeFromString<ComponentItem>(jsonString)
            } catch (e: Exception) {
                println("❌ Error parsing component JSON: $e")
                null
            }
        }
    }

    /**
     * Convierte un solo JSON string a un ComponentItem.
     */
    fun fromJsonSingle(jsonString: String): ComponentItem? {
        return try {
            json.decodeFromString<ComponentItem>(jsonString)
        } catch (e: Exception) {
            println("❌ Error parsing single component JSON: $e")
            null
        }
    }

    /**
     * Convierte un ComponentItem a JSON string (opcional pero útil para debugging o enviar al backend).
     */
    fun toJson(component: ComponentItem): String {
        return json.encodeToString(ComponentItem.serializer(), component)
    }

    /*
    fun fromJsonOld(jsonList: List<String>): List<ComponentItem> {
        val items: List<ComponentItem> = jsonList.mapNotNull { json ->
            try {
                val jsonElement = Json.parseToJsonElement(json)
                if (jsonElement is kotlinx.serialization.json.JsonObject) {
                    val map = jsonElement.mapValues { it.value.toString().removeSurrounding("\"") }
                    mapToComponentItem(map)
                } else null
            } catch (e: Exception) {
                println("Error parsing JSON: $e")
                null
            }
        }
        return items
    }

    fun mapToComponentItem(map: Map<String, Any>): ComponentItem? {
        if (map["type"] != "component_item") return null
        val id = map["id"] as? String ?: return null
        val type = map["type"] as? String ?: return null
        val text = map["text"] as? String ?: return null
        val color = map["color"] as? String ?: "#FFFFFF"
        return ComponentItem(id = id.toInt(), text, color, type)
    }*/
}