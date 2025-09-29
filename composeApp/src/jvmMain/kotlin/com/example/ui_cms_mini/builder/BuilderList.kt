package com.example.ui_cms_mini.builder

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui_cms_mini.DropTarget
import com.example.ui_cms_mini.ListViewModel

@Composable
fun BuilderList(viewModel: ListViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        DropTarget(id = 1, viewModel = viewModel)
        DropTarget(id = 2, viewModel = viewModel)
        DropTarget(id = 3, viewModel = viewModel)
        DropTarget(id = 4, viewModel = viewModel)
        DropTarget(id = 5, viewModel = viewModel)
        DropTarget(id = 6, viewModel = viewModel)
        DropTarget(id = 7, viewModel = viewModel)
    }
}

