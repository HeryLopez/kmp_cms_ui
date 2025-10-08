package com.example.ui_cms_mini.components.imageBlock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.common.model.ComponentType
import com.example.common.model.ImageComponent
import com.example.ui_cms_mini.properties.common.ColorPickerProp
import com.example.ui_cms_mini.properties.common.NumberStepperProp
import com.example.ui_cms_mini.properties.common.StyledTextField
import com.example.ui_cms_mini.properties.common.PropsGroup
import com.example.ui_cms_mini.properties.common.PropsTitle


@Composable
fun ImageBlockPropsEditor(
    component: ImageComponent,
    onUpdate: (ImageComponent) -> Unit
) {
    var title by remember(component.id) { mutableStateOf(component.title) }
    var titleColor by remember(component.id) { mutableStateOf(component.titleColor) }
    var backgroundUrl by remember(component.id) { mutableStateOf(component.backgroundImageUrl) }

    var topStartRadius by remember(component.id) { mutableStateOf(component.topStartRadius) }
    var topEndRadius by remember(component.id) { mutableStateOf(component.topEndRadius) }
    var bottomStartRadius by remember(component.id) { mutableStateOf(component.bottomStartRadius) }
    var bottomEndRadius by remember(component.id) { mutableStateOf(component.bottomEndRadius) }

    var width by remember(component.id) { mutableStateOf(component.width?.toString() ?: "") }
    var height by remember(component.id) { mutableStateOf(component.height?.toString() ?: "") }

    fun updateComponent() {
        onUpdate(
            component.copy(
                title = title,
                titleColor = titleColor,
                backgroundImageUrl = backgroundUrl,
                topStartRadius = topStartRadius,
                topEndRadius = topEndRadius,
                bottomStartRadius = bottomStartRadius,
                bottomEndRadius = bottomEndRadius,
                width = width.toFloatOrNull(),
                height = height.toFloatOrNull()
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        PropsTitle(
            title = "Editing ${ComponentType.fromType(component.type)?.title}",
            id = "ID: ${component.id}",
        )

        PropsGroup(title = "Layout") {
            StyledTextField(
                label = "Title:",
                value = title,
                onValueChange = { title = it; updateComponent() }
            )

            ColorPickerProp(
                "Title Color",
                titleColor
            ) { titleColor = it; updateComponent() }

            StyledTextField(
                label = "Background Image URL:",
                value = backgroundUrl,
                onValueChange = {
                    backgroundUrl = it; updateComponent()
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
                        updateComponent()
                    }
                )
            }
        }

        PropsGroup(title = "Dimensions") {
            StyledTextField(
                label = "Width (leave empty for max width):",
                value = width,
                onValueChange = {
                    width = it; updateComponent()
                })
            StyledTextField(
                label = "Height (leave empty for max height):",
                value = height,
                onValueChange = {
                    height = it; updateComponent()
                }
            )

        }
    }
}
