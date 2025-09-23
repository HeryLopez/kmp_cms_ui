package com.example.ui_cms_mini

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


import com.example.ui_cms_mini.listComponents.ItemListScreen
import com.example.ui_cms_mini.listComponents.ListViewModel
import com.example.ui_cms_mini.preview.PreviewMobile

fun main() = application {
    val viewModel = ListViewModel()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Ui_cms_mini",
    ) {
        Column {

            Row(

            ) {
                Box(
                    modifier = Modifier.width(320.dp)
                ) {
                    ItemListScreen(
                        viewModel = viewModel
                    )
                }
                VerticalDivider()
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    PreviewMobile(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}