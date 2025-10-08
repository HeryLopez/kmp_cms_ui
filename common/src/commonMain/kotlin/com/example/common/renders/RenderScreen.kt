package com.example.common.renders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.common.model.ButtonComponent
import com.example.common.model.ImageComponent
import com.example.common.model.LayoutNode
import com.example.common.model.Orientation
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
        RenderNode(isMainContainer = true, node = it)
    }
}


@Composable
fun RenderNode(isMainContainer: Boolean, node: LayoutNode, initialModifier: Modifier = Modifier) {
    when (node) {
        is LayoutNode.Container -> {
            if (isMainContainer) {
                val backgroundColor =
                    ColorUtils.hexToColor(node.backgroundColor) ?: Color.Transparent
                val containerModifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor)
                    .padding(node.padding.dp)

                LazyColumn(
                    modifier = containerModifier,
                    contentPadding = PaddingValues(node.contentPadding.dp),
                ) {
                    node.children.forEach {
                        item {
                            RenderNode(isMainContainer = false, it)
                        }
                    }
                }
            } else {
                val backgroundColor =
                    ColorUtils.hexToColor(node.backgroundColor) ?: Color.Transparent

                var containerModifier = initialModifier
                    .fillMaxWidth()

                containerModifier =
                    node.width?.let { containerModifier.width(it.dp) } ?: containerModifier
                containerModifier =
                    node.height?.let { containerModifier.height(it.dp) } ?: containerModifier

                containerModifier =
                    if (node.borderWidth > 0f && !node.borderColor.isNullOrEmpty()) {
                        containerModifier.border(
                            width = node.borderWidth.dp,
                            color = ColorUtils.hexToColor(node.borderColor) ?: Color.Black,
                            shape = RoundedCornerShape(
                                topStart = node.topStartRadius.dp,
                                topEnd = node.topEndRadius.dp,
                                bottomEnd = node.bottomEndRadius.dp,
                                bottomStart = node.bottomStartRadius.dp
                            )
                        )
                    } else containerModifier

                containerModifier = if (node.elevation > 0f) {
                    containerModifier.shadow(
                        elevation = node.elevation.dp,
                        shape = RoundedCornerShape(
                            topStart = node.topStartRadius.dp,
                            topEnd = node.topEndRadius.dp,
                            bottomEnd = node.bottomEndRadius.dp,
                            bottomStart = node.bottomStartRadius.dp
                        )
                    )
                } else containerModifier


                containerModifier = containerModifier
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(
                            topStart = node.topStartRadius.dp,
                            topEnd = node.topEndRadius.dp,
                            bottomEnd = node.bottomEndRadius.dp,
                            bottomStart = node.bottomStartRadius.dp
                        )
                    )

                containerModifier = containerModifier
                    .padding(node.padding.dp)


                when (node.orientation) {
                    Orientation.Row -> {
                        LazyRow(
                            modifier = containerModifier,
                            horizontalArrangement = Arrangement.spacedBy(node.spacing.dp),
                            contentPadding = PaddingValues(node.contentPadding.dp),
                        ) {
                            node.children.forEach {
                                item {
                                    RenderNode(isMainContainer = false, it)
                                }
                            }
                        }
                    }

                    Orientation.Column -> {
                        Column(
                            modifier = containerModifier,
                            verticalArrangement = Arrangement.spacedBy(node.spacing.dp),
                        ) {
                            node.children.forEach { RenderNode(isMainContainer = false, it) }
                        }
                    }

                    Orientation.Grid -> {
                        BoxWithConstraints(
                            modifier = containerModifier
                        ) {
                            val horizontalSpacing =
                                (node.flowColumns?.let { it - 1 } ?: 0) * node.spacing.dp

                            val availableWidth = maxWidth - horizontalSpacing

                            val minCellSize = node.flowMinCellSize?.dp ?: 120.dp

                            val columns =
                                node.flowColumns ?: maxOf(1, (availableWidth / minCellSize).toInt())
                            val cellWidth = availableWidth / columns

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(node.spacing.dp),
                                verticalArrangement = Arrangement.spacedBy(node.spacing.dp)
                            ) {
                                node.children.forEach {
                                    Box(modifier = Modifier.width(cellWidth)) {
                                        RenderNode(
                                            isMainContainer = false,
                                            it,
                                            initialModifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        is LayoutNode.Component -> {
            when (val c = node.component) {
                is TextComponent -> RenderTextComponent(c)
                is ImageComponent -> RenderImageComponent(
                    component = c,
                    initialModifier = initialModifier
                )

                is ButtonComponent -> RenderButtonComponent(c)
            }
        }
    }
}
