package com.example.ui_cms_mini.properties.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> OptionButtonGroup(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onSelect: (T) -> Unit,
    labelMapper: (T) -> String = { it.toString() },
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            label,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            )
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            options.forEach { option ->
                Button(
                    onClick = { onSelect(option) },
                    enabled = selectedOption != option,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(
                        horizontal = 6.dp,
                        vertical = 2.dp
                    ),
                    modifier = Modifier.defaultMinSize(minWidth = 0.dp, minHeight = 0.dp)
                ) {
                    Text(
                        text = labelMapper(option),
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
