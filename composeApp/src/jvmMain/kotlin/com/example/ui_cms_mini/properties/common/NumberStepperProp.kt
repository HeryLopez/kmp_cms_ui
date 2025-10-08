package com.example.ui_cms_mini.properties.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberStepperProp(
    label: String,
    value: Float,
    step: Float = 2f,
    min: Float = 0f,
    max: Float = 64f,
    onValueChange: (Float) -> Unit
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

        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(6.dp)
                ).padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onValueChange((value - step).coerceAtLeast(min)) },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(4.dp),
                modifier = Modifier.height(33.dp)
            ) {
                Text("-")
            }

            Box(
                modifier = Modifier.width(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${value.toInt()}",
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = { onValueChange((value + step).coerceAtMost(max)) },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(4.dp),
                modifier = Modifier.height(33.dp)
            ) {
                Text("+")
            }
        }
    }
}
