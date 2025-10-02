package com.example.ui_cms_mini.properties

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.model.ButtonComponent
import com.example.common.model.ImageComponent
import com.example.common.model.LayoutNode
import com.example.common.model.Orientation
import com.example.common.model.TextComponent
import com.example.ui_cms_mini.ListViewModel

@Composable
fun PropertiesPanel(viewModel: ListViewModel) {
    val selectedNode by viewModel.selectedNode.collectAsState()
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(horizontalScrollState)
                .verticalScroll(verticalScrollState)
                .padding(16.dp)
        ) {
            if (selectedNode == null) {
                Text("No item selected")
            } else {
                when (val node = selectedNode!!) {
                    is LayoutNode.Component -> {
                        val component = node.component

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Editing: ${component.type}")
                            when (component) {
                                is TextComponent -> TextComponentEditor(component) { updated ->
                                    viewModel.updateComponent(node.id, updated)
                                }

                                is ImageComponent -> ImageComponentEditor(component) { updated ->
                                    viewModel.updateComponent(node.id, updated)
                                }

                                is ButtonComponent -> ButtonComponentEditor(component) { updated ->
                                    viewModel.updateComponent(node.id, updated)
                                }
                            }
                        }
                    }

                    is LayoutNode.Container -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            ContainerPropsEditor(node, onUpdate = { updated ->
                                viewModel.updateContainer(node.id, updated)
                            })
                        }
                    }

                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(
                scrollState = verticalScrollState
            )
        )
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            adapter = rememberScrollbarAdapter(
                scrollState = horizontalScrollState
            )
        )
    }
}


@Composable
fun TextComponentEditor(
    component: TextComponent,
    onUpdate: (TextComponent) -> Unit
) {
    var text by remember(component.id) { mutableStateOf(component.text) }
    var color by remember(component.id) { mutableStateOf(component.color) }

    Text("Text:")
    TextField(
        value = text,
        onValueChange = {
            text = it
            onUpdate(component.copy(text = text, color = color))
        }
    )

    Text("Color (Hex):")
    TextField(
        value = color,
        onValueChange = {
            color = it
            onUpdate(component.copy(text = text, color = color))
        }
    )
}


@Composable
fun ImageComponentEditor(
    component: ImageComponent,
    onUpdate: (ImageComponent) -> Unit
) {
    var title by remember(component.id) { mutableStateOf(component.title) }
    var titleColor by remember(component.id) { mutableStateOf(component.titleColor) }
    var backgroundUrl by remember(component.id) { mutableStateOf(component.backgroundImageUrl) }

    Text("Title:")
    TextField(
        value = title,
        onValueChange = {
            title = it
            onUpdate(
                component.copy(
                    title = title,
                    titleColor = titleColor,
                    backgroundImageUrl = backgroundUrl
                )
            )
        }
    )

    Text("Title Color (Hex):")
    TextField(
        value = titleColor,
        onValueChange = {
            titleColor = it
            onUpdate(
                component.copy(
                    title = title,
                    titleColor = titleColor,
                    backgroundImageUrl = backgroundUrl
                )
            )
        }
    )

    Text("Background Image URL:")
    TextField(
        value = backgroundUrl,
        onValueChange = {
            backgroundUrl = it
            onUpdate(
                component.copy(
                    title = title,
                    titleColor = titleColor,
                    backgroundImageUrl = backgroundUrl
                )
            )
        }
    )
}


@Composable
fun ButtonComponentEditor(
    component: ButtonComponent,
    onUpdate: (ButtonComponent) -> Unit
) {
    var text by remember(component.id) { mutableStateOf(component.text) }
    var actionType by remember(component.id) { mutableStateOf(component.actionType) }
    var actionValue by remember(component.id) { mutableStateOf(component.actionValue) }

    Text("Text:")
    TextField(
        value = text,
        onValueChange = {
            text = it
            onUpdate(
                component.copy(
                    text = text,
                    actionType = actionType,
                    actionValue = actionValue
                )
            )
        }
    )

    Text("Action Type:")
    TextField(
        value = actionType,
        onValueChange = {
            actionType = it
            onUpdate(
                component.copy(
                    text = text,
                    actionType = actionType,
                    actionValue = actionValue
                )
            )
        }
    )

    Text("Action Value:")
    TextField(
        value = actionValue,
        onValueChange = {
            actionValue = it
            onUpdate(
                component.copy(
                    text = text,
                    actionType = actionType,
                    actionValue = actionValue
                )
            )
        }
    )
}
