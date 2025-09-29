package com.example.ui_cms_mini

import androidx.lifecycle.ViewModel
import com.example.common.model.ComponentItem
import com.example.common.repository.ComponentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ListViewModel : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val repo = ComponentRepository("http://localhost:9090")

    private val _items = MutableStateFlow<List<ComponentItem>>(emptyList())
    val items: StateFlow<List<ComponentItem>> = _items

    private val _jsonExport = MutableStateFlow<List<String>>(emptyList())
    val jsonExport: StateFlow<List<String>> = _jsonExport

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        initData()
    }

    fun initData() {
        viewModelScope.launch {
            _loading.value = true
            val components = repo.getAll()
            //delay(5000)
            addAllItems(components)
            _loading.value = false
        }
    }

    fun addAllItems(items: List<ComponentItem>) {
        _items.value = items.sortedBy { it.id }
        buildJson()
    }

    fun addItem(item: ComponentItem) {
        val updated = (_items.value + item).sortedBy { it.id }
        _items.value = updated
        buildJson()

        viewModelScope.launch {
            repo.saveJson(_jsonExport.value)
        }
    }

    fun removeItem(addedItem: ComponentItem) {
        val updatedList = _items.value.filter { i -> i.id != addedItem.id }
        _items.value = updatedList
        buildJson()

        viewModelScope.launch {
            repo.saveJson(_jsonExport.value)
        }
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}