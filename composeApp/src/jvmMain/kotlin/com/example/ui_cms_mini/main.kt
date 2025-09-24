package com.example.ui_cms_mini

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.ui_cms_mini.listComponents.ListViewModel
import com.example.ui_cms_mini.listComponents.generateRandomText
import com.example.common.model.ComponentItem


import kotlinx.coroutines.launch
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import kotlin.random.Random
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalDensity
import com.example.common.model.toHex
import com.example.common.repository.ComponentRepository
import com.example.ui_cms_mini.thumbnails.RandomTextThumbnail
import com.example.common.utils.ComponentJsonMapper
import com.example.ui_cms_mini.preview.PreviewMobile
import kotlinx.coroutines.Dispatchers

fun main() = application {
    val viewModel = ListViewModel()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Builder",
        state = rememberWindowState(
            width = 1280.dp,
            height = 900.dp
        )
    ) {
        MainContent(viewModel)
    }
}

@Composable
fun MainContent(viewModel: ListViewModel) {

    val repo = ComponentRepository("http://localhost:9090")
    var isLoading by remember { mutableStateOf(false) }

    // Función para cargar datos
    suspend fun loadData() {
        isLoading = true
        try {
            val jsonList = repo.getAll()
            val items: List<ComponentItem> = ComponentJsonMapper.fromJson(jsonList)
            println(">>>>  call addAllItems ${items.size}");
            viewModel.addAllItems(items)
        } catch (e: Exception) {
            println(">>>> Error ${e}");
        } finally {
            println(">>>> Finaly ");
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Black,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        }
    } else {
        Column {

            Row(

            ) {
                Box(
                    modifier = Modifier.width(320.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 120.dp), // 3 columnas fijas
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            TextComponentDragSource()
                        }
                    }
                }
                VerticalDivider()

                Box(
                    modifier = Modifier.weight(2f).fillMaxSize()
                ) {
                    Column {
                        /*
                        ItemListScreen(
                            viewModel = viewModel
                        )*/
                        DropTarget(id = 1, viewModel = viewModel)
                        DropTarget(id = 2, viewModel = viewModel)
                        DropTarget(id = 3, viewModel = viewModel)
                        DropTarget(id = 4, viewModel = viewModel)
                        DropTarget(id = 5, viewModel = viewModel)
                        DropTarget(id = 6, viewModel = viewModel)
                        DropTarget(id = 7, viewModel = viewModel)
                    }
                }
                VerticalDivider()
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    PreviewMobile(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextComponentDragSource() {
    val componentType = "component_item"
    val density = LocalDensity.current

    // Convert 120.dp to pixels for the drag scene
    val sizeDp = 120.dp
    val widthPx = with(density) { sizeDp.toPx() }.toInt()
    val heightPx = widthPx // square


    // Create an ImageComposeScene to generate the drag image
    val scene = remember {
        ImageComposeScene(
            width = widthPx,
            height = heightPx,
            density = density,
            coroutineContext = Dispatchers.Unconfined
        ) {
            RandomTextThumbnail() // Composable that renders the thumbnail
        }
    }

    // Render the scene to a ComposeImageBitmap
    val dragImage = remember { scene.render().toComposeImageBitmap() }


    val img = scene.render()
    // Convertir a BufferedImage compatible con AWT
    val bufferedI2mage = img.toComposeImageBitmap()


    // 🔴 Drag source
    Box(
        modifier = Modifier
            .size(sizeDp) // UI size
            .fillMaxWidth()

            .dragAndDropSource(
                drawDragDecoration = {
                    // Draw the image that will appear during dragging
                    drawImage(dragImage)
                }
            ) { offset ->

                DragAndDropTransferData(
                    transferable = DragAndDropTransferable(
                        StringSelection(componentType)
                    ),

                    // List of actions supported by this drag source. A type of action
                    // is passed to the drop target together with data.
                    // The target can use this to reject an inappropriate drop operation
                    // or to interpret user expectations.
                    supportedActions = listOf(
                        DragAndDropTransferAction.Copy,
                        DragAndDropTransferAction.Move,
                        DragAndDropTransferAction.Link,
                    ),
                    dragDecorationOffset = offset,
                    onTransferCompleted = { action ->
                        println("Action at the source: $action")
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        RandomTextThumbnail()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropTarget(id: Int, viewModel: ListViewModel) {

    val scope = rememberCoroutineScope()
    val addedItem by viewModel.items.collectAsState().let { state ->
        derivedStateOf { state.value.firstOrNull { it.id == id } }
    }

    println(">>>> Find ${id} - ${addedItem?.id} - ${addedItem?.text}")

    var showTargetBackground by remember { mutableStateOf(false) }
    var showTargetBorder by remember { mutableStateOf(false) }
    var targetText by remember { mutableStateOf("Drop Here") }

    val dragAndDropTarget = remember {
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
                targetText = event.awtTransferable.let {
                    if (it.isDataFlavorSupported(DataFlavor.stringFlavor))
                        it.getTransferData(DataFlavor.stringFlavor) as String
                    else
                        it.transferDataFlavors.first().humanPresentableName
                }


                // Add item
                val text = generateRandomText()
                val color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                )
                val item = ComponentItem(id, text, color.toHex(), "component_item")
              //  addedItem = item
                viewModel.addItem( item)
                scope.launch {
                    val repo = ComponentRepository("http://localhost:9090")
                    repo.save(item)
                }


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
                )
,
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(addedItem!!.colorColor)
                    .padding(8.dp),
                contentAlignment = Alignment.Center

            ) {
                Text(addedItem!!.text, color = Color.White)
            }
        }


    } else {
        // 🔴 Drag source
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