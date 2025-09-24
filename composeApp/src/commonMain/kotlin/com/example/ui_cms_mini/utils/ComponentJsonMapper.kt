package com.example.ui_cms_mini.utils


import com.example.ui_cms_mini.model.ComponentItem
import kotlinx.serialization.json.Json


object ComponentJsonMapper {
    fun fromJson(jsonList: List<String>): List<ComponentItem> {
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
    }

}