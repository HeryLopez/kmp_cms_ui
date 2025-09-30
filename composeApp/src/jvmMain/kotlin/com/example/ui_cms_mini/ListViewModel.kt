package com.example.ui_cms_mini

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.common.model.ComponentItem
import com.example.common.model.ComponentType
import com.example.common.model.ImageComponent
import com.example.common.model.TextComponent
import com.example.common.repository.ComponentRepository
import com.example.common.utils.toHex
import com.example.ui_cms_mini.builder.BuilderUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.random.Random

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

    fun addItem(dropId: Int, componentType: String) {
        // Creamos un componente dummy según el tipo
        val item: ComponentItem = when (componentType) {
            ComponentType.TEXT_BLOCK.type -> {
                val text = BuilderUtils.generateRandomText()
                val color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                )
                TextComponent(
                    id = dropId,
                    text = text,
                    color = color.toHex()
                )
            }

            ComponentType.IMAGE_BLOCK.type -> {
                val title = BuilderUtils.generateRandomText()
                val titleColor = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                )
                ImageComponent(
                    id = dropId,
                    title = title,
                    titleColor = titleColor.toHex(),
                    backgroundImageUrl = "https://picsum.photos/300/200?random=${Random.nextInt(1000)}"
                )
            }

            else -> throw IllegalArgumentException("Unknown component type: $componentType")
        }

        // Agregar al estado
        val updated = (_items.value + item).sortedBy { it.id }
        _items.value = updated

        // Reconstruir JSON
        buildJson()

        // Guardar en repo
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