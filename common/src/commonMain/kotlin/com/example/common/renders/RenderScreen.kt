package com.example.common.renders

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.common.model.ComponentItem
import com.example.common.model.ImageComponent
import com.example.common.model.TextComponent
import com.example.common.platform
import com.example.common.utils.ComponentJsonMapper
import com.example.common.utils.textColorForContrast

@Composable
fun RenderScreen(jsonList: List<String>) {
    val items: List<ComponentItem> = ComponentJsonMapper.fromJson(jsonList)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(2f)
        ) {
            items(items) { item ->
                when (item) {
                    is TextComponent -> RenderTextComponent(item)
                    is ImageComponent -> RenderImageComponent(item)
                    // 🧩 En el futuro: simplemente agregas
                    // is ButtonComponent -> RenderButtonComponent(item)
                }
            }
        }
    }
}


@Composable
fun RenderTextComponent(component: TextComponent) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = component.colorColor)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = component.text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = component.colorColor.textColorForContrast()
            )
        )
    }
}

@Composable
fun RenderImageComponent(component: ImageComponent) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        AsyncImage(
            model = component.backgroundImageUrl,
            contentDescription = component.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(component.titleColorColor.copy(alpha = 0.4f))
        )
        Text(
            text = component.title,
            color = component.titleColorColor,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

