package com.example.ui_cms_mini.components.textBlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.model.ComponentType
import com.example.common.model.TextComponent
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


    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        PropsTitle(
            title = "Editing ${ComponentType.fromType(component.type)?.title}",
            id = "ID: ${component.id}",
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StyledTextField(
                label = "Text:",
                singleLine = false,
                value = text,
                onValueChange = {
                    text = it
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
            )

            StyledTextField(
                label = "Text Color (Hex):",
                value = textColor ?: "",
                onValueChange = {
                    textColor = it
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
            )

            StyledTextField(
                label = "Background Color (Hex):",
                value = backgroundColor ?: "",
                onValueChange = {
                    backgroundColor = it
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
            )


            StyledTextField(
                label = "Font Size (sp):",
                value = fontSize, onValueChange = {
                    fontSize = it
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
            )

            // Peso de fuente
            Text(
                "Font Weight:",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                listOf("Normal", "Bold", "Medium", "Light", "Thin", "Black").forEach { option ->
                    Button(
                        onClick = {
                            fontWeight = option
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
                        },
                        enabled = fontWeight != option
                    ) {
                        Text(
                            option,
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }

            // Estilo de fuente
            Text(
                "Font Style:",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                listOf("Normal", "Italic").forEach { option ->
                    Button(
                        onClick = {
                            fontStyle = option
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
                        },
                        enabled = fontStyle != option
                    ) {
                        Text(
                            option,
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }


            Text("Text Alignment:")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("start", "center", "end").forEach { align ->
                    Button(
                        onClick = {
                            textAlign = align
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
                        },

                        enabled = textAlign != align
                    ) {
                        Text(
                            align,
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }
        }
    }
}
