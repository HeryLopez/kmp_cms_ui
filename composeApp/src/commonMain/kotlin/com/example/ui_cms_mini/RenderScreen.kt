package com.example.ui_cms_mini

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui_cms_mini.model.ComponentItem
import com.example.ui_cms_mini.utils.ComponentJsonMapper

@Composable
fun RenderScreen(jsonList: List<String>) {
    // Parseamos cada JSON de la lista a ComponentItem
    val items: List<ComponentItem> = ComponentJsonMapper.fromJson(jsonList)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = item.colorColor)
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = item.text,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = item.colorColor.textColorForContrast()
                        )
                    )
                }
            }
        }

        if (getPlatform().name == "desktop") {
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "JSON generado:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))

                // TextField de solo lectura para copiar/pegar
                TextField(
                    value = jsonList.joinToString(separator = "\n"),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxSize(),
                    singleLine = false,
                    maxLines = 20
                )
            }
        }
    }
}

fun Color.textColorForContrast(): Color {
    // Calcula luminancia aproximada: https://stackoverflow.com/a/1855903
    val luminance = (0.299 * red + 0.587 * green + 0.114 * blue)
    return if (luminance > 0.5) Color.Black else Color.White
}