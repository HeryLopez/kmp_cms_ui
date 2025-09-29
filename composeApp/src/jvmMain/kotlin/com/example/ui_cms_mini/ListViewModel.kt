package com.example.ui_cms_mini

import com.example.common.model.ComponentItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json

class ListViewModel {
    private val _items = MutableStateFlow<List<ComponentItem>>(emptyList())
    val items: StateFlow<List<ComponentItem>> = _items

    private val _jsonExport = MutableStateFlow<List<String>>(emptyList())
    val jsonExport: StateFlow<List<String>> = _jsonExport

    fun addAllItems(items: List<ComponentItem>) {
        _items.value = items.sortedBy { it.id }
        buildJson()
    }

    fun addItem(item: ComponentItem) {
        val updated = (_items.value + item).sortedBy { it.id }
        _items.value = updated
        buildJson()
    }

    private fun buildJson() {
        _jsonExport.value = _items.value
            .sortedBy { it.id }
            .map { item ->
                val mapWithId = item.toMap().toMutableMap()
                mapWithId["id"] = item.id.toString()
                Json.Default.encodeToString(mapWithId)
            }
    }
}