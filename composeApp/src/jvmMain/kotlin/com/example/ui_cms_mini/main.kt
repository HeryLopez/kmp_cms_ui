package com.example.ui_cms_mini

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.common.model.ImageComponent
import com.example.common.model.TextComponent
import com.example.ui_cms_mini.builder.BuilderList
import com.example.ui_cms_mini.common.composables.HorizontalResizeHandle
import com.example.ui_cms_mini.common.composables.PanelHeader
import com.example.ui_cms_mini.common.composables.VerticalResizeHandle
import com.example.ui_cms_mini.components.imageBlock.ImageBlockDragSource
import com.example.ui_cms_mini.components.randomText.TextBlockDragSource
import com.example.ui_cms_mini.preview.PreviewMobile

fun main() = application {
    val viewModel = ListViewModel()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Builder",
        state = rememberWindowState(
            width = 1480.dp,
            height = 1100.dp
        )
    ) {
        MainContent(viewModel)
    }
}

@Composable
fun MainContent(viewModel: ListViewModel) {

    var componentsPanelWidth by remember { mutableStateOf(320.dp) }
    var propsPanelWidth by remember { mutableStateOf(300.dp) }
    var previewPanelWidth by remember { mutableStateOf(400.dp) }
    var consoleHigh by remember { mutableStateOf(200.dp) }

    val componentsMin = 200.dp
    val componentsMax = 500.dp
    val centerMin = 200.dp
    val centerMax = 500.dp
    val propsMin = 200.dp
    val propsMax = 500.dp
    val previewMin = 400.dp
    val previewMax = 500.dp

    val consoleMin = 200.dp
    val consoleMax = 400.dp

    val componentsColor = Color.Gray.copy(alpha = 0.1f)
    val propsColor = Color.White
    val previewColor = Color.Gray.copy(alpha = 0.1f)


    val isLoading by viewModel.loading.collectAsState()

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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // ---------------------------------------------------------------------
                // Components panel
                // ---------------------------------------------------------------------
                Box(
                    modifier = Modifier
                        .width(componentsPanelWidth)
                        .fillMaxHeight()
                        .background(color = componentsColor)
                ) {
                    Column {
                        PanelHeader(title = "Components", backgroundColor = Color.Transparent)
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 120.dp),
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                TextBlockDragSource()
                            }
                            item {
                                ImageBlockDragSource()
                            }
                        }
                    }
                }

                VerticalResizeHandle(
                    color = componentsColor,
                    onDrag = { delta ->
                        val newWidth =
                            (componentsPanelWidth + delta).coerceIn(componentsMin, componentsMax)
                        componentsPanelWidth = newWidth
                    }
                )

                // ---------------------------------------------------------------------
                // Builder grid panel
                // ---------------------------------------------------------------------
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    Column {
                        PanelHeader(title = "Builder Grid", backgroundColor = Color.White)
                        Box {
                            val scrollState = rememberScrollState()

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                                    .padding(6.dp)
                            ) {
                                BuilderList(
                                    viewModel = viewModel,
                                    modifier = Modifier.widthIn(min = centerMin, max = centerMax)
                                )
                            }

                            VerticalScrollbar(
                                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                                adapter = rememberScrollbarAdapter(
                                    scrollState = scrollState
                                )
                            )

                        }
                    }
                }

                // ---------------------------------------------------------------------
                // Properties Panel
                // ---------------------------------------------------------------------
                VerticalResizeHandle(
                    color = propsColor,
                    onDrag = { delta ->
                        val newWidth = (propsPanelWidth - delta).coerceIn(propsMin, propsMax)
                        propsPanelWidth = newWidth
                    }
                )

                Box(
                    modifier = Modifier
                        .width(propsPanelWidth)
                        .fillMaxHeight()
                        .background(propsColor)
                ) {
                    Column {
                        PanelHeader(title = "Properties", backgroundColor = propsColor)
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                        ) {
                            PropertiesPanel(viewModel)
                        }
                    }
                }

                // ---------------------------------------------------------------------
                // Preview Panel
                // ---------------------------------------------------------------------
                VerticalResizeHandle(
                    color = previewColor,
                    onDrag = { delta ->
                        val newWidth = (previewPanelWidth - delta).coerceIn(previewMin, previewMax)
                        previewPanelWidth = newWidth
                    }
                )

                Box(
                    modifier = Modifier
                        .width(previewPanelWidth)
                        .fillMaxHeight()
                        .background(previewColor)
                ) {
                    Column {
                        PanelHeader(title = "Preview", backgroundColor = Color.Transparent)
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                        ) {
                            PreviewMobile(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }

            // ---------------------------------------------------------------------
            // Console Panel
            // ---------------------------------------------------------------------
            HorizontalResizeHandle(
                color = previewColor,
                onDrag = { delta ->
                    val newHigh = (consoleHigh - delta).coerceIn(consoleMin, consoleMax)
                    consoleHigh = newHigh
                }
            )
            Box(
                modifier = Modifier
                    .height(consoleHigh)
                    .fillMaxWidth()
                    .background(previewColor)
            ) {
                Column {
                    PanelHeader(title = "Console", backgroundColor = Color.Transparent)
                    ConsoleTextField(viewModel = viewModel)
                }
            }

        }
    }
}

@Composable
fun ConsoleTextField(viewModel: ListViewModel, modifier: Modifier = Modifier) {
    val value by viewModel.jsonExport.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(0.dp) // padding visual del panel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            BasicTextField(
                value = value.joinToString(separator = "\n"),
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) 
            )
        }
    }
}

@Composable
fun PropertiesPanel(viewModel: ListViewModel) {
    val selected by viewModel.selectedItem.collectAsState()
    println("Render propsPanel item=${selected?.id}")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (selected == null) {
            Text("No item selected")
        } else {
            val selectedItem = selected!!
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Editing: ${selectedItem.type}")

                when (selectedItem) {
                    is TextComponent -> {
                        var text by remember(selectedItem.id) { mutableStateOf(selectedItem.text) }
                        var color by remember(selectedItem.id) { mutableStateOf(selectedItem.color) }


                        Text("Text:")
                        androidx.compose.material3.TextField(
                            value = text,
                            onValueChange = {
                                text = it
                                viewModel.updateItem(selectedItem.copy(text = text, color = color))

                            }
                        )

                        Text("Color (Hex):")
                        androidx.compose.material3.TextField(
                            value = color,
                            onValueChange = {
                                color = it
                                viewModel.updateItem(selectedItem.copy(text = text, color = color))
                            }
                        )
                    }

                    is ImageComponent -> {
                        var title by remember(selectedItem.id) { mutableStateOf(selectedItem.title) }
                        var titleColor by remember(selectedItem.id) { mutableStateOf(selectedItem.titleColor) }
                        var backgroundUrl by remember(selectedItem.id) { mutableStateOf(selectedItem.backgroundImageUrl) }

                        Text("Title:")
                        androidx.compose.material3.TextField(
                            value = title,
                            onValueChange = {
                                title = it
                                viewModel.updateItem(
                                    selectedItem.copy(
                                        title = title,
                                        titleColor = titleColor,
                                        backgroundImageUrl = backgroundUrl
                                    )
                                )
                            }
                        )

                        Text("Title Color (Hex):")
                        androidx.compose.material3.TextField(
                            value = titleColor,
                            onValueChange = {
                                titleColor = it
                                viewModel.updateItem(
                                    selectedItem.copy(
                                        title = title,
                                        titleColor = titleColor,
                                        backgroundImageUrl = backgroundUrl
                                    )
                                )
                            }
                        )

                        Text("Background Image URL:")
                        androidx.compose.material3.TextField(
                            value = backgroundUrl,
                            onValueChange = {
                                backgroundUrl = it
                                viewModel.updateItem(
                                    selectedItem.copy(
                                        title = title,
                                        titleColor = titleColor,
                                        backgroundImageUrl = backgroundUrl
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
