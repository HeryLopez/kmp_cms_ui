package com.example.ui_cms_mini

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.common.model.ComponentItem
import com.example.common.repository.ComponentRepository
import com.example.common.utils.ComponentJsonMapper
import com.example.ui_cms_mini.builder.BuilderList
import com.example.ui_cms_mini.common.composables.VerticalResizeHandle
import com.example.ui_cms_mini.preview.PreviewMobile
import com.example.ui_cms_mini.thumbnails.randomText.RandomTextDragSource

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

    var leftPanelWidth by remember { mutableStateOf(320.dp) }
    var rightPanelWidth by remember { mutableStateOf(400.dp) }

    val leftMin = 200.dp
    val leftMax = 500.dp

    val centerMin = 200.dp
    val centerMax = 500.dp

    val rightMin = 400.dp
    val rightMax = 500.dp

    val leftColor = Color.Gray.copy(alpha = 0.1f)
    val rightColor = Color.Gray.copy(alpha = 0.1f)


    val repo = ComponentRepository("http://localhost:9090")
    var isLoading by remember { mutableStateOf(false) }

    suspend fun loadData() {
        isLoading = true
        try {
            val jsonList = repo.getAll()
            val items: List<ComponentItem> = ComponentJsonMapper.fromJson(jsonList)
            viewModel.addAllItems(items)
        } catch (e: Exception) {
            // TODO Show Error
        } finally {
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
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Components panel
            Box(
                modifier = Modifier
                    .width(leftPanelWidth)
                    .fillMaxHeight()
                    .background(color = leftColor)
                    .padding(6.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        RandomTextDragSource()
                    }
                }
            }

            // Separator
            VerticalResizeHandle(
                color = leftColor,
                onDrag = { delta ->
                    val newWidth = (leftPanelWidth + delta).coerceIn(leftMin, leftMax)
                    leftPanelWidth = newWidth
                }
            )

            // Builder grid panel
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState())
                    .padding(6.dp)
            ) {
                BuilderList(
                    viewModel = viewModel,
                    modifier = Modifier.widthIn(min = centerMin, max = centerMax)
                )
            }

            // Separator
            VerticalResizeHandle(
                color = rightColor,
                onDrag = { delta ->
                    val newWidth = (rightPanelWidth - delta).coerceIn(rightMin, rightMax)
                    rightPanelWidth = newWidth
                }
            )

            // Preview panel
            Box(
                modifier = Modifier
                    .width(rightPanelWidth)
                    .fillMaxHeight()
                    .background(rightColor)
                    .padding(6.dp)
            ) {
                PreviewMobile(
                    viewModel = viewModel
                )
            }
        }
    }
}


