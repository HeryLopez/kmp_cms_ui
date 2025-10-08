package com.example.ui_cms_mini

import IconButtonDesktop
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.ui_cms_mini.builder.BuilderList
import com.example.ui_cms_mini.common.composables.HorizontalResizeHandle
import com.example.ui_cms_mini.common.composables.PanelHeader
import com.example.ui_cms_mini.common.composables.VerticalResizeHandle
import com.example.ui_cms_mini.components.containerBlock.ContainerBlockDragSource
import com.example.ui_cms_mini.components.imageBlock.ImageBlockDragSource
import com.example.ui_cms_mini.components.textBlock.TextBlockDragSource
import com.example.ui_cms_mini.console.ConsolePanel
import com.example.ui_cms_mini.preview.PreviewMobile
import com.example.ui_cms_mini.properties.PropertiesPanel
import ui_cms_mini.composeapp.generated.resources.Res
import ui_cms_mini.composeapp.generated.resources.preview_icon
import ui_cms_mini.composeapp.generated.resources.props_icon

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

    var componentsPanelWidth by remember { mutableStateOf(170.dp) }
    var propsPanelWidth by remember { mutableStateOf(400.dp) }
    var previewPanelWidth by remember { mutableStateOf(400.dp) }
    var consoleHigh by remember { mutableStateOf(0.dp) }

    val componentsMin = 170.dp
    val componentsMax = 310.dp
    val centerMin = 200.dp
    val centerMax = 500.dp


    val previewMin = 0.dp
    val previewMax = 450.dp

    val consoleMin = 0.dp
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
                                ContainerBlockDragSource()
                            }
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

                            Box(
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
                        val newWidth = (propsPanelWidth - delta).coerceIn(Constants.PROPS_MIN, Constants.PROPS_MAX)
                        propsPanelWidth = newWidth
                    }
                )

                Box(

                ) {
                    Column(
                        modifier = Modifier
                            .width(propsPanelWidth)
                            .fillMaxHeight()
                            .background(propsColor)
                    ) {
                        PanelHeader(title = "Properties", backgroundColor = propsColor)
                        PropertiesPanel(viewModel, propsPanelWidth)
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
                                .horizontalScroll(rememberScrollState())
                                .verticalScroll(rememberScrollState())
                        ) {
                            PreviewMobile(viewModel = viewModel)
                        }
                    }
                }

                // ---------------------------------------------------------------------
                // Panel management Panel
                // ---------------------------------------------------------------------
                Box(
                    modifier = Modifier
                        .width(75.dp)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButtonDesktop(
                            resource = Res.drawable.preview_icon,
                            text = "Preview",
                            rippleRounded = 6.dp,
                            expand = true,
                            onClick = {
                                previewPanelWidth = if (previewPanelWidth > 0.dp) 0.dp else previewMax
                            }

                        )
                        IconButtonDesktop(
                            resource = Res.drawable.props_icon,
                            text = "Properties",
                            rippleRounded = 6.dp,
                            expand = true,
                            onClick = {
                                propsPanelWidth = if (propsPanelWidth > 0.dp) 0.dp else Constants.PROPS_MAX
                            }
                        )
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
            Box {
                Column {
                    PanelHeader(
                        title = "Console", backgroundColor = Color.Transparent,
                        onClick = {
                            consoleHigh = if (consoleHigh > 0.dp) 0.dp else 200.dp
                        }
                    )

                    Box(
                        modifier = Modifier
                            .height(consoleHigh)
                            .fillMaxWidth()
                            .background(previewColor)
                    ) {
                        ConsolePanel(viewModel = viewModel)
                    }

                }
            }
        }
    }
}
