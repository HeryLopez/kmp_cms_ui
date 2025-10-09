package com.example.ui_cms_mini.components.textBlock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.common.model.ComponentType
import com.example.common.model.TextComponent
import com.example.ui_cms_mini.properties.common.ColorPickerProp
import com.example.ui_cms_mini.properties.common.NumberStepperProp
import com.example.ui_cms_mini.properties.common.OptionButtonGroup
import com.example.ui_cms_mini.properties.common.PropsGroup
import com.example.ui_cms_mini.properties.common.PropsTitle
import com.example.ui_cms_mini.properties.common.StyledTextField

@Composable
fun TextBlockPropsEditor(
    component: TextComponent,
    onUpdate: (TextComponent) -> Unit
) {
    var text by remember(component.id) { mutableStateOf(component.text) }
    var textColor by remember(component.id) { mutableStateOf(component.textColor) }
    var backgroundColor by remember(component.id) { mutableStateOf(component.backgroundColor) }

    var fontSize by remember(component.id) { mutableStateOf(component.fontSize) }
    var fontWeight by remember(component.id) { mutableStateOf(component.fontWeight) }
    var fontStyle by remember(component.id) { mutableStateOf(component.fontStyle) }
    var textAlign by remember(component.id) { mutableStateOf(component.textAlign) }

    var paddingValue by remember(component.id) { mutableStateOf(component.padding) }

    fun updateComponent() {
        onUpdate(
            component.copy(
                text = text,
                textColor = textColor,
                backgroundColor = backgroundColor,
                padding = paddingValue,
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontStyle = fontStyle,
                textAlign = textAlign
            )
        )
    }

    Column(
        Modifier.fillMaxWidth(),
    ) {
        PropsTitle(
            title = "Editing ${ComponentType.fromType(component.type)?.title}",
            id = "ID: ${component.id}",
        )

        PropsGroup(title = "Content") {
            StyledTextField(
                label = "Text:",
                singleLine = false,
                value = text,
                onValueChange = { text = it; updateComponent() }
            )

            NumberStepperProp(
                label = "Padding",
                value = paddingValue,
                onValueChange = {
                    paddingValue = it; updateComponent()
                }
            )
        }


        PropsGroup(title = "Color") {
            ColorPickerProp(
                "Text Color",
                textColor
            ) { textColor = it; updateComponent() }

            ColorPickerProp(
                "Background Color",
                backgroundColor
            ) { backgroundColor = it; updateComponent() }
        }

        PropsGroup(title = "Typography") {

            NumberStepperProp(
                label = "Font Size,",
                value = fontSize,
                step = 1f,
                max = 128f,
                onValueChange = {
                    fontSize = it; updateComponent()
                }
            )

            OptionButtonGroup(
                label = "Font Weight:",
                options = listOf("Normal", "Bold", "Medium", "Light", "Thin", "Black"),
                selectedOption = fontWeight,
                onSelect = { fontWeight = it; updateComponent() }
            )

            OptionButtonGroup(
                label = "Font Style:",
                options = listOf("Normal", "Italic"),
                selectedOption = fontStyle,
                onSelect = { fontStyle = it; updateComponent() }
            )
        }

        PropsGroup(title = "Alignment") {
            OptionButtonGroup(
                label = "Font Alignment:",
                options = listOf("Start", "Center", "End"),
                selectedOption = textAlign,
                onSelect = { textAlign = it; updateComponent() }
            )
        }
    }
}
