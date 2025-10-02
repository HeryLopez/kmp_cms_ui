package com.example.ui_cms_mini.builder

import IconButtonDesktop
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import ui_cms_mini.composeapp.generated.resources.Res
import ui_cms_mini.composeapp.generated.resources.delete_icon
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

    val isSelected = rootNode == selectedNode

    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)
        .fillMaxSize()
        /*
        .background(
            if (showTargetBackground) Color.LightGray else Color.White,
            RoundedCornerShape(8.dp)
        )
        .border(
            BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2F)),
            shape = RoundedCornerShape(8.dp)
        )*/
        .border(
            BorderStroke(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) Color(0xFF1976D2) else Color.Gray.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(8.dp)
        )
        .background(
            if (isSelected) Color(0xFFE3F2FD) else Color.Transparent,
            shape = RoundedCornerShape(8.dp)
        )

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
                        cornerRadius = CornerRadius(4.dp.toPx()),
                        style = Stroke(
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
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
        ) {
            if (!isSelected) {
                viewModel.selectItem(rootNode)
            } else {
                viewModel.clearSelection()
            }
        }

    //val isRoot = rootNode.id == viewModel.rootNode.value.id


    if (rootNode.orientation == Orientation.Row) {
        Row(modifier = modifier) {
            ContainerHeader(
                onRemoveClick = {}
            )
            rootNode.children.forEach { node ->
                RenderNode(rootNode, node, selectedNode, viewModel)
            }
            ContainerDropZone()
        }
    } else {
        Column(modifier = modifier) {
            ContainerHeader(
                onRemoveClick = {}
            )
            rootNode.children.forEach { node ->
                RenderNode(rootNode, node, selectedNode, viewModel)
            }
            ContainerDropZone()
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
    val isSelected = node == selectedNode
    when (node) {
        is LayoutNode.Component -> {
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .border(
                        BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) Color(0xFF1976D2) else Color.Gray.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        if (isSelected) Color(0xFFE3F2FD) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) {
                        if (!isSelected) {
                            viewModel.selectItem(node)
                        } else {
                            viewModel.clearSelection()
                        }
                    }
            ) {
                ComponentHeader(
                    text = "${ComponentType.fromType(node.component.type)?.title}",
                    onRemoveClick = {
                        viewModel.removeComponentFromContainer(
                            parentNode.id,
                            node
                        )
                    }
                )
                when (node.component) {
                    is TextComponent -> TextBlockDropComponent(
                        component = node.component as TextComponent
                    )

                    is ImageComponent -> ImageBlockDropComponent(
                        component = node.component as ImageComponent
                    )

                    is ButtonComponent -> Button(onClick = {}) {
                        Text("Botón")
                    }
                }
            }
        }

        is LayoutNode.Container -> LayoutContainer(
            rootNode = node,
            selectedNode = selectedNode,
            viewModel = viewModel
        )
    }
}
