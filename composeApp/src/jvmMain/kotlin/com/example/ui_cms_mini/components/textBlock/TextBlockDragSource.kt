package com.example.ui_cms_mini.components.textBlock

import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.common.model.NodeType
import kotlinx.coroutines.Dispatchers
import java.awt.datatransfer.StringSelection


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextBlockDragSource() {
    val density = LocalDensity.current

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
            TextBlockThumbnail() // Composable that renders the thumbnail
        }
    }

    // Render the scene to a ComposeImageBitmap
    val dragImage = remember { scene.render().toComposeImageBitmap() }

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
                        StringSelection(NodeType.TEXT_BLOCK.type)
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
        TextBlockThumbnail()
    }
}
