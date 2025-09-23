package com.example.ui_cms_mini.listComponents

import com.example.ui_cms_mini.model.ComponentItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListViewModel {
    private val _items = MutableStateFlow<List<ComponentItem>>(emptyList())
    val items: StateFlow<List<ComponentItem>> = _items

    private val _jsonExport = MutableStateFlow<List<String>>(emptyList())
    val jsonExport: StateFlow<List<String>> = _jsonExport

    fun addItem(item: ComponentItem) {
        _items.value = _items.value + item

        buildJson()
    }

    private fun buildJson() {
        // Serializamos cada item a JSON y guardamos en la lista
        _jsonExport.value = _items.value.map { item ->
            Json.encodeToString(item.toMap())
        }
    }
}
