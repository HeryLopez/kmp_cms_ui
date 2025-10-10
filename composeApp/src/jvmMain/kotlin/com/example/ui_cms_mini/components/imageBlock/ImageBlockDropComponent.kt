package com.example.ui_cms_mini.components.imageBlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.common.model.ImageComponent
import com.example.common.renders.RenderImageComponent

@Composable
fun ImageBlockDropComponent(component: ImageComponent) {

    val phoneWidth = 800f
    val phoneHeight = 600f

    val widthValue = component.width ?: (component.height?.let { it * phoneWidth / phoneHeight } ?: phoneWidth)
    val heightValue = component.height ?: (component.width?.let { it * phoneHeight / phoneWidth } ?: phoneHeight)

    val aspectRatio = widthValue / heightValue

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RenderImageComponent(
            component,
            initialModifier = Modifier
                .heightIn(max = 60.dp)
                .aspectRatio(aspectRatio)
        )
    }
}