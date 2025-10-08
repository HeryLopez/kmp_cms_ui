package com.example.ui_cms_mini.components.containerBlock

import androidx.compose.runtime.Composable
import com.example.common.model.LayoutNode
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.common.model.Orientation
import com.example.ui_cms_mini.properties.common.ColorPickerProp
import com.example.ui_cms_mini.properties.common.OptionButtonGroup
import com.example.ui_cms_mini.properties.common.NumberStepperProp
import com.example.ui_cms_mini.properties.common.PropsGroup
import com.example.ui_cms_mini.properties.common.PropsTitle
import com.example.ui_cms_mini.properties.common.StyledTextField

@Composable
fun ContainerBlockPropsEditor(
    container: LayoutNode.Container,
    onUpdate: (LayoutNode.Container) -> Unit
) {
    var orientation by remember(container.id) { mutableStateOf(container.orientation) }

    var width by remember(container.id) { mutableStateOf(container.width?.toString() ?: "") }
    var height by remember(container.id) { mutableStateOf(container.height?.toString() ?: "") }

    var paddingValue by remember(container.id) { mutableStateOf(container.padding) }
    var contentPaddingValue by remember(container.id) { mutableStateOf(container.contentPadding) }

    var backgroundColor by remember(container.id) { mutableStateOf(container.backgroundColor) }
    var borderColor by remember(container.id) { mutableStateOf(container.borderColor) }
    var borderWidth by remember(container.id) { mutableStateOf(container.borderWidth) }

    var topStartRadius by remember(container.id) { mutableStateOf(container.topStartRadius) }
    var topEndRadius by remember(container.id) { mutableStateOf(container.topEndRadius) }
    var bottomStartRadius by remember(container.id) { mutableStateOf(container.bottomStartRadius) }
    var bottomEndRadius by remember(container.id) { mutableStateOf(container.bottomEndRadius) }

    var elevation by remember(container.id) { mutableStateOf(container.elevation) }
    var spacing by remember(container.id) { mutableStateOf(container.spacing) }

    var flowMinCellSize by remember(container.id) {
        mutableStateOf(
            container.flowMinCellSize?.toString() ?: ""
        )
    }
    var flowColumns by remember(container.id) {
        mutableStateOf(
            container.flowColumns?.toString() ?: ""
        )
    }


    fun updateContainer() {
        onUpdate(
            container.copy(
                orientation = orientation,
                width = width.toFloatOrNull(),
                height = height.toFloatOrNull(),
                padding = paddingValue,
                contentPadding = contentPaddingValue,
                backgroundColor = backgroundColor,
                borderColor = borderColor,
                borderWidth = borderWidth,
                topStartRadius = topStartRadius,
                topEndRadius = topEndRadius,
                bottomStartRadius = bottomStartRadius,
                bottomEndRadius = bottomEndRadius,
                elevation = elevation,
                spacing = spacing,
                flowMinCellSize = flowMinCellSize.toFloatOrNull(),
                flowColumns = flowColumns.toIntOrNull(),
            )
        )
    }

    Column(
        Modifier
            .fillMaxWidth(),
    ) {
        PropsTitle(title = "Editing Container", id = "ID: ${container.id}")

        PropsGroup(title = "Layout") {
            OptionButtonGroup(
                label = "Orientation:",
                options = listOf(
                    Orientation.Row,
                    Orientation.Column,
                    Orientation.Grid,
                ),
                selectedOption = orientation,
                onSelect = { orientation = it; updateContainer() }
            )


            // Width & Height
            StyledTextField("Width (leave empty for max width)", width) {
                width = it; updateContainer()
            }
            StyledTextField("Height (leave empty for max height)", height) {
                height = it; updateContainer()
            }

            NumberStepperProp(
                label = "Padding",
                value = paddingValue,
                onValueChange = {
                    paddingValue = it; updateContainer()
                }
            )
        }

        PropsGroup(title = "Appearance") {
            ColorPickerProp(
                "Background Color",
                backgroundColor
            ) { backgroundColor = it; updateContainer() }

            ColorPickerProp(
                "Border Color",
                borderColor
            ) { borderColor = it; updateContainer() }

            NumberStepperProp(
                label = "Border Width",
                value = borderWidth,
                step = 1f,
                onValueChange = {
                    borderWidth = it; updateContainer()
                }
            )

            NumberStepperProp(
                label = "Elevation",
                value = elevation,
                onValueChange = {
                    elevation = it; updateContainer()
                }
            )

            NumberStepperProp(
                label = "Spacing",
                value = spacing,
                onValueChange = {
                    spacing = it; updateContainer()
                }
            )
        }

        PropsGroup(title = "Corner Radii") {
            listOf(
                "Top Start" to topStartRadius,
                "Top End" to topEndRadius,
                "Bottom Start" to bottomStartRadius,
                "Bottom End" to bottomEndRadius
            ).forEach { (label, value) ->
                NumberStepperProp(
                    label = label,
                    value = value,
                    onValueChange = {
                        val newVal = it
                        when (label) {
                            "Top Start" -> topStartRadius = newVal
                            "Top End" -> topEndRadius = newVal
                            "Bottom Start" -> bottomStartRadius = newVal
                            "Bottom End" -> bottomEndRadius = newVal
                        }
                        updateContainer()
                    }
                )
            }
        }

        PropsGroup(title = "Row") {
            NumberStepperProp(
                label = "Content Padding",
                value = contentPaddingValue,
                onValueChange = {
                    contentPaddingValue = it; updateContainer()
                }
            )
        }

        PropsGroup(title = "Grid") {
            StyledTextField("Min Cell Size (dp)", flowMinCellSize) {
                flowMinCellSize = it; updateContainer()
            }
            StyledTextField("Columns", flowColumns) { flowColumns = it; updateContainer() }
        }
    }
}