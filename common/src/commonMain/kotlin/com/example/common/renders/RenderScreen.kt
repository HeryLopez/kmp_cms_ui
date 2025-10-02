package com.example.common.renders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.common.model.ButtonComponent
import com.example.common.model.ImageComponent
import com.example.common.model.LayoutNode
import com.example.common.model.TextComponent
import com.example.common.utils.ColorUtils
import com.example.common.utils.ComponentJsonMapper
import com.example.common.utils.textColorForContrast

@Composable
fun RenderScreen(json: String) {
    val layoutNode: LayoutNode? = try {
        ComponentJsonMapper.fromJson(json)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    layoutNode?.let {
        RenderNode(it)
    }
}


@Composable
fun RenderNode(node: LayoutNode) {
    when (node) {
        is LayoutNode.Container -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = ColorUtils.hexToColor(node.backgroundColor))
                    .padding(node.padding.dp)
            ) {
                node.children.forEach { RenderNode(it) }
            }
        }

        is LayoutNode.Component -> {
            when (val c = node.component) {
                is TextComponent -> RenderTextComponent(c)
                is ImageComponent -> RenderImageComponent(c)
                is ButtonComponent -> RenderButtonComponent(c)
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

@Composable
fun RenderButtonComponent(component: ButtonComponent) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2), // Azul por defecto
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = component.text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
