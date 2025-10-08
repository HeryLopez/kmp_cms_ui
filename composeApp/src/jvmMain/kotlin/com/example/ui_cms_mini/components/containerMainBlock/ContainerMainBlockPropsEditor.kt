package com.example.ui_cms_mini.components.containerMainBlock

import androidx.compose.runtime.Composable
import com.example.common.model.LayoutNode
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui_cms_mini.properties.common.ColorPickerProp
import com.example.ui_cms_mini.properties.common.NumberStepperProp
import com.example.ui_cms_mini.properties.common.PropsGroup
import com.example.ui_cms_mini.properties.common.PropsTitle

@Composable
fun ContainerMainBlockPropsEditor(
    container: LayoutNode.Container,
    onUpdate: (LayoutNode.Container) -> Unit
) {
    var paddingValue by remember(container.id) { mutableStateOf(container.padding) }
    var contentPaddingValue by remember(container.id) { mutableStateOf(container.contentPadding) }

    var backgroundColor by remember(container.id) { mutableStateOf(container.backgroundColor) }

    fun updateContainer() {
        onUpdate(
            container.copy(
                padding = paddingValue,
                contentPadding = contentPaddingValue,
                backgroundColor = backgroundColor,
            )
        )
    }

    Column(
        Modifier
            .fillMaxWidth(),
    ) {
        PropsTitle(title = "Editing Main Container", id = "ID: ${container.id}")

        PropsGroup(title = "Layout") {
            NumberStepperProp(
                label = "Padding",
                value = paddingValue,
                onValueChange = {
                    paddingValue = it; updateContainer()
                }
            )

            NumberStepperProp(
                label = "Content Padding",
                value = contentPaddingValue,
                onValueChange = {
                    contentPaddingValue = it; updateContainer()
                }
            )
        }


        PropsGroup(title = "Appearance") {
            ColorPickerProp(
                "Background Color",
                backgroundColor
            ) { backgroundColor = it; updateContainer() }

        }
    }
}