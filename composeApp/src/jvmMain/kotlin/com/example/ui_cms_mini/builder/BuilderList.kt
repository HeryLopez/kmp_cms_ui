package com.example.ui_cms_mini.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui_cms_mini.DropTarget
import com.example.ui_cms_mini.ListViewModel

@Composable
fun BuilderList(viewModel: ListViewModel, modifier: Modifier = Modifier) {

    val items by viewModel.items.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()

    Column(modifier = modifier) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            println("Render ${item.id}")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSelected) Color(0xFFE0E0E0) else Color.Transparent)
                    .padding(8.dp)
                    .clickable { viewModel.selectItem(item) } // Aquí selecciona
            ) {
                DropTarget(id = item.id, viewModel = viewModel)
            }
        }
        println("Render ${items.size + 1}")
        DropTarget(id = items.size + 1, viewModel = viewModel)
    }
}

