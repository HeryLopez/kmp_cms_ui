package com.example.ui_cms_mini

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.example.common.model.ImageComponent
import com.example.common.model.TextComponent
import com.example.ui_cms_mini.components.imageBlock.ImageBlockDropComponent
import com.example.ui_cms_mini.components.randomText.TextBlockDropComponent
import java.awt.datatransfer.DataFlavor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropTarget(id: Int, viewModel: ListViewModel) {

    val addedItem by viewModel.items.collectAsState().let { state ->
        derivedStateOf { state.value.firstOrNull { it.id == id } }
    }

    var showTargetBackground by remember { mutableStateOf(false) }
    var showTargetBorder by remember { mutableStateOf(false) }
    var targetText by remember { mutableStateOf("Drop Here") }

    val dragAndDropTarget = remember(id) {
        object : DragAndDropTarget {

            // Highlights the border of a potential drop target
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

                val result = (targetText == "Drop here")

                // Changes the text to the value dropped into the composable.
                val componentType = event.awtTransferable.let {
                    if (it.isDataFlavorSupported(DataFlavor.stringFlavor))
                        it.getTransferData(DataFlavor.stringFlavor) as String
                    else
                        it.transferDataFlavors.first().humanPresentableName
                }

                // Update selected item
                targetText = componentType
                viewModel.addItem(id, componentType)
/*
                // Add item
                val text = BuilderUtils.generateRandomText()
                val color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                )
                val item = ComponentItem(id, text, color.toHex(), "component_item")
                //  addedItem = item
                viewModel.addItem(item)

*/
                // Reverts the text of the drop target to the initial
                // value after 2 seconds.
                /* coroutineScope.launch {
                     delay(2000)
                     targetText = "Drop here"
                 }*/
                return result
            }
        }
    }

    if (addedItem != null) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(if (showTargetBackground) Color.LightGray else Color.White)
                .border(BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2F)))
                .padding(16.dp)

                .then(
                    if (showTargetBorder)
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
                ),
            contentAlignment = Alignment.Center
        ) {
            val component = addedItem!!;
            when (component) {
                is TextComponent -> TextBlockDropComponent(
                    component = component,
                    onRemoveClick = { viewModel.removeItem(component) })

                is ImageComponent -> ImageBlockDropComponent(
                    component = component,
                    onRemoveClick = { viewModel.removeItem(component) })
            }
        }

    } else {
        // Drag source
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(if (showTargetBackground) Color.LightGray.copy(alpha = 0.3F) else Color.White)
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
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(targetText)
        }
    }
}

