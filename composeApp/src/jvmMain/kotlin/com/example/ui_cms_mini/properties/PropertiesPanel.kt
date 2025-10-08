package com.example.ui_cms_mini.properties

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.common.model.ButtonComponent
import com.example.common.model.ImageComponent
import com.example.common.model.LayoutNode
import com.example.common.model.TextComponent
import com.example.ui_cms_mini.ListViewModel
import com.example.ui_cms_mini.components.buttonBlock.ButtonBlockPropsEditor
import com.example.ui_cms_mini.components.containerBlock.ContainerBlockPropsEditor
import com.example.ui_cms_mini.components.containerMainBlock.ContainerMainBlockPropsEditor
import com.example.ui_cms_mini.components.imageBlock.ImageBlockPropsEditor
import com.example.ui_cms_mini.components.textBlock.TextBlockPropsEditor

@Composable
fun PropertiesPanel(viewModel: ListViewModel, width: Dp) {
    val selectedNode by viewModel.selectedNode.collectAsState()
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(horizontalScrollState)
                .verticalScroll(verticalScrollState)
        ) {
            if (selectedNode == null) {
                Box(
                    // It's important to set min width to avoid deforming content
                    modifier = Modifier
                        .widthIn(min = 200.dp)
                        .width(width)
                        .padding(16.dp)
                ) {
                    Text("No item selected")
                }
            } else {
                val node = selectedNode!!.first
                val isMainNode = selectedNode!!.second

                when (val node = node) {
                    is LayoutNode.Component -> {
                        val component = node.component

                        Row(
                            // It's important to set min width to avoid deforming content
                            modifier = Modifier
                                .widthIn(min = 200.dp)
                                .width(width)
                        ) {
                            when (component) {
                                is TextComponent -> TextBlockPropsEditor(component) { updated ->
                                    viewModel.updateComponent(node.id, updated)
                                }

                                is ImageComponent -> ImageBlockPropsEditor(component) { updated ->
                                    viewModel.updateComponent(node.id, updated)
                                }

                                is ButtonComponent -> ButtonBlockPropsEditor(component) { updated ->
                                    viewModel.updateComponent(node.id, updated)
                                }
                            }
                        }
                    }

                    is LayoutNode.Container -> {

                        Box(
                            // It's important to set min width to avoid deforming content
                            modifier = Modifier
                                .widthIn(min = 200.dp)
                                .width(width)
                        ) {
                            if (isMainNode) {
                                ContainerMainBlockPropsEditor(
                                    container = node,
                                    onUpdate = { updated ->
                                        viewModel.updateContainer(node.id, updated)
                                    }
                                )
                            } else {
                                ContainerBlockPropsEditor(
                                    container = node,
                                    onUpdate = { updated ->
                                        viewModel.updateContainer(node.id, updated)
                                    }
                                )
                            }
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
