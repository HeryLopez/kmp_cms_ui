package com.example.ui_cms_mini.properties

import androidx.compose.runtime.Composable
import com.example.common.model.LayoutNode
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.model.Orientation
import com.example.common.utils.toHex

@Composable
fun ContainerPropsEditor(
    container: LayoutNode.Container,
    onUpdate: (LayoutNode.Container) -> Unit
) {
    var orientation by remember(container.id) { mutableStateOf(container.orientation) }
    var backgroundColor by remember(container.id) { mutableStateOf(container.backgroundColor) }
    var paddingValue by remember(container.id) { mutableStateOf(container.padding) }


    Column {
        Text("Editing Container", style = MaterialTheme.typography.titleMedium)
        Text("ID: ${container.id}", style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray, fontSize = 10.sp))

        Spacer(modifier = Modifier.height(12.dp))

        // Selector de orientación
        Text("Orientation")
        Row {
            listOf(Orientation.Row, Orientation.Column).forEach { option ->
                Button(
                    onClick = {
                        orientation = option
                        onUpdate(container.copy(
                            orientation = orientation,
                            backgroundColor = backgroundColor,
                            padding = paddingValue
                        ))
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (orientation == option) Color.Gray else Color.LightGray
                    )
                ) {
                    Text(option.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selector de color de fondo
        Text("Background Color")
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val colors = listOf(Color.White, Color.LightGray, Color.Red, Color.Green, Color.Blue)
            colors.forEach { colorOption ->
                val colorHex = colorOption.toHex()
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(colorOption, shape = RoundedCornerShape(4.dp))
                        .border(
                            width = if (backgroundColor == colorHex) 2.dp else 1.dp,
                            color = if (backgroundColor == colorHex) Color.Black else Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            backgroundColor = colorHex
                            onUpdate(container.copy(
                                orientation = orientation,
                                backgroundColor = backgroundColor,
                                padding = paddingValue
                            ))
                        }
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Padding
        Text("Padding: ${paddingValue.toInt()} dp")
        Row {
            Button(onClick = {
                paddingValue = (paddingValue + 4f).coerceAtMost(64f)
                onUpdate(container.copy(
                    orientation = orientation,
                    backgroundColor = backgroundColor,
                    padding = paddingValue
                ))
            }) { Text("+") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                paddingValue = (paddingValue - 4f).coerceAtLeast(0f)
                onUpdate(container.copy(
                    orientation = orientation,
                    backgroundColor = backgroundColor,
                    padding = paddingValue
                ))
            }) { Text("-") }
        }
    }
}