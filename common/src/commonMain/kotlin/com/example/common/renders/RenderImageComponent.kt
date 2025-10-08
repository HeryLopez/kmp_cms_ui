package com.example.common.renders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.common.model.ImageComponent
import org.jetbrains.compose.resources.painterResource
import ui_cms_mini.common.generated.resources.Res
import ui_cms_mini.common.generated.resources.image_placeholder

@Composable
fun RenderImageComponent(component: ImageComponent, initialModifier: Modifier = Modifier) {
    val titleColor = component.titleColorValue ?: Color.Black

    val cornerShape = RoundedCornerShape(
        topStart = component.topStartRadius.dp,
        topEnd = component.topEndRadius.dp,
        bottomEnd = component.bottomEndRadius.dp,
        bottomStart = component.bottomStartRadius.dp
    )

    var modifier = initialModifier.clip(cornerShape)

    modifier = if (component.width != null) {
        modifier.width(component.width.dp)
    } else {
        modifier.fillMaxWidth()
    }

    modifier = if (component.height != null) {
        modifier.height(component.height.dp)
    } else {
        modifier.wrapContentHeight()
    }

    val placeholderPainter = painterResource(Res.drawable.image_placeholder)

    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = component.backgroundImageUrl,
            contentDescription = component.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = placeholderPainter,
            error = placeholderPainter,
        )

     //   if (component.titleColorValue != null)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(titleColor.copy(alpha = 0.3f))
            ) {

                Text(
                    text = component.title,
                    color = titleColor,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

    }
}