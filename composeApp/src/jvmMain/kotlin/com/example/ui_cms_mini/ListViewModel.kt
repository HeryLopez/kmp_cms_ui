package com.example.ui_cms_mini

import androidx.lifecycle.ViewModel
import com.example.common.model.ButtonComponent
import com.example.common.model.ComponentItem
import com.example.common.model.ImageComponent
import com.example.common.model.TextComponent
import com.example.common.repository.ComponentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import com.example.common.model.LayoutNode
import com.example.common.model.NodeType
import com.example.common.utils.ComponentJsonMapper
import kotlin.String

class ListViewModel : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val repo = ComponentRepository("http://localhost:9090")

    private val _rootNode = MutableStateFlow(LayoutNode.Container())
    val rootNode: StateFlow<LayoutNode.Container> = _rootNode

    private val _selectedNode = MutableStateFlow<Pair<LayoutNode, Boolean>?>(null)
    val selectedNode = _selectedNode

    private val _jsonExport = MutableStateFlow("")
    val jsonExport: StateFlow<String> = _jsonExport

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    init {
        initData()
    }


    fun selectItem(node: LayoutNode, isMainContainer: Boolean) {
        _selectedNode.value = Pair(node, isMainContainer)
    }

    fun clearSelection() {
        _selectedNode.value = null
    }

    fun LayoutNode.Container.deepCopy(): LayoutNode.Container {
        return LayoutNode.Container(
            id = this.id,
            orientation = this.orientation,
            children = this.children.map { child ->
                when (child) {
                    is LayoutNode.Container -> child.deepCopy()
                    is LayoutNode.Component -> child.copy()
                }
            }
        )
    }

    fun addComponentToContainer(containerId: String, type: NodeType) {
        val newNode = createComponent(type)
        val newRoot = addNodeToContainer(_rootNode.value, containerId, newNode)
        _rootNode.value = newRoot

        buildJson()
        syncInServer()
    }

    fun removeNode(containerId: String) {
        val newRoot = removeNodeById(_rootNode.value, containerId)
        _rootNode.value = newRoot
        buildJson()
        syncInServer()
    }

    private fun findContainerById(
        root: LayoutNode.Container,
        containerId: String
    ): LayoutNode.Container? {
        if (root.id == containerId) return root
        for (child in root.children) {
            if (child is LayoutNode.Container) {
                val found = findContainerById(child, containerId)
                if (found != null) return found
            }
        }
        return null
    }


    fun addNodeToContainer(
        root: LayoutNode.Container,
        containerId: String,
        newNode: LayoutNode
    ): LayoutNode.Container {
        if (root.id == containerId) {
            return root.copy(children = root.children + newNode)
        }

        val newChildren = root.children.map { child ->
            if (child is LayoutNode.Container) {
                val updatedChild = addNodeToContainer(child, containerId, newNode)
                if (updatedChild !== child) {
                    return@map updatedChild
                }
            }
            return@map child
        }

        return if (newChildren !== root.children) {
            root.copy(children = newChildren)
        } else {
            root
        }
    }

    private fun removeNodeById(root: LayoutNode.Container, nodeIdToRemove: String): LayoutNode.Container {
        val newChildren = root.children.filter { it.nodeId != nodeIdToRemove }

        if (newChildren.size < root.children.size) {
            return root.copy(children = newChildren)
        }

        val childrenAfterRecursiveRemoval = root.children.map { child ->
            if (child is LayoutNode.Container) {
                val updatedChild = removeNodeById(child, nodeIdToRemove)
                if (updatedChild !== child) {
                    return@map updatedChild
                }
            }
            return@map child
        }

        return if (childrenAfterRecursiveRemoval !== root.children) {
            root.copy(children = childrenAfterRecursiveRemoval)
        } else {
            root
        }
    }

    private fun updateNodeRecursive(
        node: LayoutNode,
        nodeIdToUpdate: String,
        transform: (LayoutNode.Component) -> LayoutNode.Component
    ): LayoutNode {
        when (node) {
            is LayoutNode.Component -> {
                return if (node.id == nodeIdToUpdate) {
                    transform(node)
                } else {
                    node
                }
            }

            is LayoutNode.Container -> {
                var changed = false
                val newChildren = node.children.map { child ->
                    val updatedChild = updateNodeRecursive(child, nodeIdToUpdate, transform)

                    if (updatedChild !== child) {
                        changed = true
                    }
                    return@map updatedChild
                }

                return if (changed) {
                    node.copy(children = newChildren)
                } else {
                    node
                }
            }
        }
    }

    private fun createComponent(type: NodeType): LayoutNode {

        val nodeId = LayoutNode.generateId()

        val item: LayoutNode = when (type) {
            NodeType.CONTAINER -> {
                LayoutNode.Container()
            }

            NodeType.TEXT_BLOCK -> {
                LayoutNode.Component(
                    component = TextComponent(
                        id = nodeId,
                        text = "Text",
                    )
                )
            }

            NodeType.IMAGE_BLOCK -> {
                LayoutNode.Component(
                    component = ImageComponent(
                        id = nodeId,
                        title = "",
                        backgroundImageUrl = "https://picsum.photos/id/${
                            Random.nextInt(
                                from = 0,
                                1084
                            )
                        }/600/400"
                    )
                )
            }

            NodeType.BUTTON_BLOCK -> {
                LayoutNode.Component(
                    component = ButtonComponent(
                        id = nodeId,
                        text = "Text",
                        actionType = "link",
                        actionValue = "https://www.google.com"
                    )
                )
            }
        }
        return item
    }


    fun updateComponent(nodeId: String, updated: ComponentItem) {
        val newRoot = updateNodeRecursive(_rootNode.value, nodeId) { componentNode ->
            componentNode.copy(component = updated)
        }

        if (newRoot !== _rootNode.value) {
            _rootNode.value = newRoot as LayoutNode.Container
        }
        buildJson()
        syncInServer()
    }


    private fun updateContainerRecursive(
        node: LayoutNode,
        containerIdToUpdate: String,
        transform: (LayoutNode.Container) -> LayoutNode.Container
    ): LayoutNode {
        return when (node) {
            is LayoutNode.Component -> node
            is LayoutNode.Container -> {
                var changed = false

                val newChildren = node.children.map { child ->
                    val updatedChild =
                        updateContainerRecursive(child, containerIdToUpdate, transform)
                    if (updatedChild !== child) changed = true
                    updatedChild
                }

                val updatedNode = if (node.id == containerIdToUpdate) {
                    transform(node.copy(children = newChildren))
                } else if (changed) {
                    node.copy(children = newChildren)
                } else node

                updatedNode
            }
        }
    }


    fun updateContainer(containerId: String, updated: LayoutNode.Container) {
        val newRoot = updateContainerRecursive(_rootNode.value, containerId) {
            updated
        }

        if (newRoot !== _rootNode.value) {
            _rootNode.value = newRoot as LayoutNode.Container
        }

        buildJson()
        syncInServer()
    }


    fun moveNode(nodeId: String, moveUp: Boolean) {
        val newRoot = moveNodeRecursive(_rootNode.value, nodeId, moveUp)
        if (newRoot !== _rootNode.value) {
            _rootNode.value = newRoot
            buildJson()
            syncInServer()
        }
    }

    private fun moveNodeRecursive(
        node: LayoutNode.Container,
        nodeId: String,
        moveUp: Boolean
    ): LayoutNode.Container {
        var changed = false

        val newChildren = node.children.toMutableList()

        val index = newChildren.indexOfFirst { it.nodeId == nodeId }

        if (index != -1) {
            val targetIndex = if (moveUp) index - 1 else index + 1
            if (targetIndex in newChildren.indices) {

                val temp = newChildren[targetIndex]
                newChildren[targetIndex] = newChildren[index]
                newChildren[index] = temp
                changed = true
            }
        } else {
            val updatedChildren = newChildren.map { child ->
                if (child is LayoutNode.Container) {
                    val updatedChild = moveNodeRecursive(child, nodeId, moveUp)
                    if (updatedChild !== child) changed = true
                    updatedChild
                } else child
            }
            if (changed) return node.copy(children = updatedChildren)
        }

        return if (changed) node.copy(children = newChildren) else node
    }

    fun manualInitData(jsonString: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (jsonString.isNotBlank()) {
                    val container = ComponentJsonMapper.fromJson(jsonString)
                    _rootNode.value = container
                } else {
                    _rootNode.value = LayoutNode.Container()
                }
                buildJson()
            } catch (e: Exception) {
                e.printStackTrace()
                _rootNode.value = LayoutNode.Container()
            } finally {
                _loading.value = false
            }
        }
    }

    fun initData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val jsonString = repo.getJson()
                if (jsonString.isNotBlank()) {
                    val container = ComponentJsonMapper.fromJson(jsonString)
                    _rootNode.value = container
                } else {
                    _rootNode.value = LayoutNode.Container()
                }
                buildJson()
            } catch (e: Exception) {
                e.printStackTrace()
                _rootNode.value = LayoutNode.Container()
            } finally {
                _loading.value = false
            }
        }
    }

    private fun syncInServer() {
        viewModelScope.launch {
            try {
                repo.saveJson(_jsonExport.value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun buildJson() {

        val json = ComponentJsonMapper.toJson(_rootNode.value)
        _jsonExport.value = json
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}