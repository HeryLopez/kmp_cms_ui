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
            // Mapeamos los hijos para crear nuevas copias inmutables
            children = this.children.map { child ->
                when (child) {
                    // Copia recursiva para Container
                    is LayoutNode.Container -> child.deepCopy()
                    // Copia de datos (shallow copy) para Componente
                    is LayoutNode.Component -> child.copy()
                }
            }
        )
    }

    fun addComponentToContainer(containerId: String, type: NodeType) {
        val newNode = createComponent(type)
        // 1. Creamos la nueva jerarquía inmutable
        val newRoot = addNodeToContainer(_rootNode.value, containerId, newNode)
        // 2. Reemplazamos la raíz, forzando la emisión del StateFlow
        _rootNode.value = newRoot

        buildJson()
        syncInServer()
    }

    fun removeComponentFromContainer(containerId: String, component: LayoutNode.Component) {
        // Usamos la función de eliminación recursiva
        val newRoot = removeNodeById(_rootNode.value, component.id)
        // Reemplazamos la raíz
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

    /**
     * Inserta un nuevo nodo en el contenedor especificado.
     *
     * NOTA: Esta función DEBE RECONSTRUIR la ruta desde el nodo modificado
     * hasta la raíz para mantener la inmutabilidad.
     */
    fun addNodeToContainer(
        root: LayoutNode.Container,
        containerId: String,
        newNode: LayoutNode
    ): LayoutNode.Container {
        if (root.id == containerId) {
            // Encontramos el contenedor. Creamos una nueva instancia
            // con la nueva lista de hijos.
            return root.copy(children = root.children + newNode)
        }

        // Recorremos los hijos recursivamente.
        val newChildren = root.children.map { child ->
            if (child is LayoutNode.Container) {
                // Intentamos modificar el hijo Container
                val updatedChild = addNodeToContainer(child, containerId, newNode)
                if (updatedChild !== child) {
                    // Si el hijo fue modificado, devolvemos el hijo modificado
                    return@map updatedChild
                }
            }
            // Devolvemos el hijo sin cambios
            return@map child
        }

        // Si la lista de hijos ha cambiado (porque se modificó uno de sus descendientes),
        // creamos una copia de la raíz con los nuevos hijos.
        return if (newChildren !== root.children) {
            root.copy(children = newChildren)
        } else {
            root
        }
    }

    /**
     * Elimina un nodo por su ID del contenedor.
     */
    private fun removeNodeById(root: LayoutNode.Container, nodeIdToRemove: String): LayoutNode.Container {
        // 1. Intentamos filtrar al nodo de los hijos directos
        val newChildren = root.children.filter { it.nodeId != nodeIdToRemove }

        // 2. Si la cantidad de hijos ha cambiado, significa que el nodo fue eliminado aquí.
        if (newChildren.size < root.children.size) {
            return root.copy(children = newChildren)
        }

        // 3. Si no se eliminó aquí, navegamos recursivamente.
        val childrenAfterRecursiveRemoval = root.children.map { child ->
            if (child is LayoutNode.Container) {
                val updatedChild = removeNodeById(child, nodeIdToRemove)
                if (updatedChild !== child) {
                    return@map updatedChild // Devolvemos el contenedor hijo actualizado
                }
            }
            return@map child // Devolvemos el nodo sin cambios
        }

        // 4. Si la lista de hijos cambió a través de la recursión, copiamos la raíz.
        return if (childrenAfterRecursiveRemoval !== root.children) {
            root.copy(children = childrenAfterRecursiveRemoval)
        } else {
            root
        }
    }


    /**
     * Función que busca un nodo por ID y aplica una transformación,
     * devolviendo la nueva sub-jerarquía inmutable.
     *
     * @param node El nodo actual (puede ser Container o Component).
     * @param nodeIdToUpdate El ID del nodo a buscar.
     * @param transform La lambda que define cómo crear la nueva copia del nodo encontrado.
     * @return El nuevo nodo (o la nueva sub-jerarquía) si hubo un cambio, o el nodo original si no.
     */
    private fun updateNodeRecursive(
        node: LayoutNode,
        nodeIdToUpdate: String,
        transform: (LayoutNode.Component) -> LayoutNode.Component
    ): LayoutNode {
        when (node) {
            is LayoutNode.Component -> {
                // Caso Base: Encontramos el componente
                return if (node.id == nodeIdToUpdate) {
                    // Aplicamos la transformación y devolvemos la nueva copia del Component
                    transform(node)
                } else {
                    // Devolvemos el nodo original si no coincide
                    node
                }
            }

            is LayoutNode.Container -> {
                var changed = false
                // 1. Mapeamos los hijos para crear la nueva lista
                val newChildren = node.children.map { child ->
                    // Llamada recursiva para procesar los hijos
                    val updatedChild = updateNodeRecursive(child, nodeIdToUpdate, transform)

                    // Verificamos si la referencia del hijo ha cambiado
                    if (updatedChild !== child) {
                        changed = true
                    }
                    return@map updatedChild
                }

                // 2. Si algún hijo ha cambiado, creamos una nueva copia del Container
                return if (changed) {
                    node.copy(children = newChildren)
                } else {
                    // Si no hay cambios en la sub-rama, devolvemos el Container original
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
        // La lambda de transformación crea el nuevo componente inmutable
        val newRoot = updateNodeRecursive(_rootNode.value, nodeId) { componentNode ->
            // Aquí creamos la nueva instancia del Component con el nuevo 'component'
            componentNode.copy(component = updated)
        }

        // 1. Comprobamos si la referencia de la raíz ha cambiado (indicando que hubo una actualización)
        if (newRoot !== _rootNode.value) {
            // 2. Reemplazamos la raíz, forzando la emisión del StateFlow y la recomposición
            _rootNode.value = newRoot as LayoutNode.Container

            // buildJson()
            // syncInServer()
        }
        buildJson()
        syncInServer()
    }


    /**
     * Actualiza un Container de manera recursiva por su ID.
     */
    private fun updateContainerRecursive(
        node: LayoutNode,
        containerIdToUpdate: String,
        transform: (LayoutNode.Container) -> LayoutNode.Container
    ): LayoutNode {
        return when (node) {
            is LayoutNode.Component -> node // No hacemos nada con los componentes
            is LayoutNode.Container -> {
                var changed = false

                val newChildren = node.children.map { child ->
                    val updatedChild =
                        updateContainerRecursive(child, containerIdToUpdate, transform)
                    if (updatedChild !== child) changed = true
                    updatedChild
                }

                // Si este es el container que queremos actualizar
                val updatedNode = if (node.id == containerIdToUpdate) {
                    transform(node.copy(children = newChildren))
                } else if (changed) {
                    node.copy(children = newChildren)
                } else node

                updatedNode
            }
        }
    }

    /**
     * Método público para actualizar un Container desde ViewModel
     */
    fun updateContainer(containerId: String, updated: LayoutNode.Container) {
        val newRoot = updateContainerRecursive(_rootNode.value, containerId) {
            // Aquí reemplazamos el container con la nueva versión
            updated
        }

        // Si cambió, reemplazamos la raíz
        if (newRoot !== _rootNode.value) {
            _rootNode.value = newRoot as LayoutNode.Container
        }

        buildJson()
        syncInServer()
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