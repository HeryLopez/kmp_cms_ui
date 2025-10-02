package com.example.ui_cms_mini.builder

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.example.common.model.ButtonComponent
import com.example.common.model.ComponentType
import com.example.common.model.ImageComponent
import com.example.common.model.LayoutNode
import com.example.common.model.NodeType
import com.example.common.model.Orientation
import com.example.common.model.TextComponent
import com.example.ui_cms_mini.ListViewModel
import com.example.ui_cms_mini.components.imageBlock.ImageBlockDropComponent
import com.example.ui_cms_mini.components.randomText.TextBlockDropComponent
import java.awt.datatransfer.DataFlavor

@Composable
fun BuilderList(viewModel: ListViewModel, modifier: Modifier = Modifier) {
    val rootNode by viewModel.rootNode.collectAsState()
    val selectedNode by viewModel.selectedNode.collectAsState()

    // TODO need to use LazyRow o ColumnRow
    println("Render BuilderList")
    Box(
        modifier = modifier.fillMaxHeight()//.background(Color.Red)
    ) {
        LayoutContainer(
            rootNode = rootNode,
            selectedNode = selectedNode,
            viewModel = viewModel
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LayoutContainer(
    rootNode: LayoutNode.Container,
    selectedNode: LayoutNode?,
    viewModel: ListViewModel
) {
    println("Render LayoutContainer ${rootNode.id}")
    var showTargetBackground by remember { mutableStateOf(false) }
    var showTargetBorder by remember { mutableStateOf(false) }

    val dragAndDropTarget = remember(rootNode.id) {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                showTargetBorder = true
            }

            override fun onEnded(event: DragAndDropEvent) {
                showTargetBorder = false
                showTargetBackground = false
            }

            override fun onEntered(event: DragAndDropEvent) {
                showTargetBackground = true
            }

            override fun onExited(event: DragAndDropEvent) {
                showTargetBackground = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {

                // Prints the type of action into system output every time
                // a drag-and-drop operation is concluded.
                println("Action at the target: ${event.action}")


                // Changes the text to the value dropped into the composable.
                val typeString = event.awtTransferable.let {
                    if (it.isDataFlavorSupported(DataFlavor.stringFlavor))
                        it.getTransferData(DataFlavor.stringFlavor) as String
                    else
                        it.transferDataFlavors.first().humanPresentableName
                }

                val type = NodeType.fromType(typeString)
                if (type != null) {
                    viewModel.addComponentToContainer(rootNode.id, type = type)
                }

                return false
            }
        }
    }

    val modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
        .background(
            if (showTargetBackground) Color.LightGray else Color.White,
            RoundedCornerShape(4.dp)
        )
        .border(BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2F)))
        .then(
            if (!showTargetBackground && showTargetBorder)
                Modifier.border(BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3F)))
            else
                Modifier
        )
        .then(
            if (showTargetBackground)
                Modifier.drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val dash = floatArrayOf(10f, 10f)
                    drawRoundRect(
                        color = Color.Blue.copy(alpha = 0.6f),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = strokeWidth,
                            pathEffect = PathEffect.dashPathEffect(dash, 0f)
                        )
                    )
                }
            else Modifier
        )
        .dragAndDropTarget(
            // With "true" as the value of shouldStartDragAndDrop,
            // drag-and-drop operations are enabled unconditionally.
            shouldStartDragAndDrop = { true },
            target = dragAndDropTarget
        )

    //val isRoot = rootNode.id == viewModel.rootNode.value.id


    if (rootNode.orientation == Orientation.Row) {
        Row(modifier = modifier) {
            RenderChildren(rootNode, selectedNode, viewModel)
        }
    } else {
        Column(modifier = modifier) {
            RenderChildren(rootNode, selectedNode, viewModel)
        }
    }
}

@Composable
fun RenderNode(
    parentNode: LayoutNode.Container,
    node: LayoutNode,
    selectedNode: LayoutNode?,
    viewModel: ListViewModel
) {
    when (node) {
        is LayoutNode.Component -> RenderComponent(parentNode.id, node, viewModel)
        is LayoutNode.Container -> LayoutContainer(
            rootNode = node,
            selectedNode = selectedNode,
            viewModel = viewModel
        )
    }
}

@Composable
private fun RenderChildren(
    rootNode: LayoutNode.Container,
    selectedNode: LayoutNode?,
    viewModel: ListViewModel,
) {
    rootNode.children.forEach { node ->
        val isSelected = node == selectedNode

        val childModifier = Modifier
            .padding(4.dp)
            .border(
                BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) Color(0xFF1976D2) else Color.Gray.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                if (isSelected) Color(0xFFE3F2FD) else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                if (isSelected) {
                    viewModel.clearSelection()
                } else {
                    viewModel.selectItem(node)
                }
            }

        Box(modifier = childModifier) {
            RenderNode(rootNode, node, selectedNode, viewModel)
        }
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
    ) {
        Text("Drop item here")
    }
}

@Composable
fun RenderComponent(parentId: String, component: LayoutNode.Component, viewModel: ListViewModel) {
    when (component.component) {
        is TextComponent -> TextBlockDropComponent(
            component = component.component as TextComponent,
            onRemoveClick = { viewModel.removeComponentFromContainer(parentId, component) }
        )

        is ImageComponent -> ImageBlockDropComponent(
            component = component.component as ImageComponent,
            onRemoveClick = { viewModel.removeComponentFromContainer(parentId, component) }
        )

        is ButtonComponent -> Button(onClick = {}) {
            Text("Botón")
        }
    }
}
