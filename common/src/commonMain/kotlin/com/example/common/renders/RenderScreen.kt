package com.example.common.renders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.model.ButtonComponent
import com.example.common.model.ImageComponent
import com.example.common.model.LayoutNode
import com.example.common.model.TextComponent
import com.example.common.utils.ColorUtils
import com.example.common.utils.ComponentJsonMapper

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
            val backgroundColor = ColorUtils.hexToColor(node.backgroundColor) ?: Color.Transparent
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor)
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
