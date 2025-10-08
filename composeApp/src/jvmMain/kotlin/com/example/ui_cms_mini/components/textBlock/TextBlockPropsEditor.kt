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
import com.example.common.utils.ColorUtils
import com.example.ui_cms_mini.properties.common.ColorPickerProp
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

    var fontSize by remember(component.id) {
        mutableStateOf(
            component.fontSize?.toString() ?: "14"
        )
    }
    var fontWeight by remember(component.id) { mutableStateOf(component.fontWeight ?: "normal") }
    var fontStyle by remember(component.id) { mutableStateOf(component.fontStyle ?: "normal") }
    var textAlign by remember(component.id) { mutableStateOf(component.textAlign ?: "start") }

    fun updateComponent() {
        onUpdate(
            component.copy(
                text = text,
                textColor = textColor,
                backgroundColor = backgroundColor,
                fontSize = fontSize.toFloatOrNull(),
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

            ColorPickerProp(
                "Background Color",
                backgroundColor
            ) { backgroundColor = it; updateComponent() }
        }


        PropsGroup(title = "Color") {
            ColorPickerProp(
                "Text Color",
                textColor
            ) { textColor = it; updateComponent() }
        }

        PropsGroup(title = "Typography") {
            StyledTextField(
                label = "Font Size (sp):",
                value = fontSize,
                onValueChange = { fontSize = it; updateComponent() }
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
                options = listOf("start", "center", "end"),
                selectedOption = textAlign,
                onSelect = { textAlign = it; updateComponent() }
            )
        }
    }
}
