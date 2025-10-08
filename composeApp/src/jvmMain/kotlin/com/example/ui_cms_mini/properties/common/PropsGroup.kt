package com.example.ui_cms_mini.properties.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PropsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider(color = Color.LightGray)

        Column(
            modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                ),
            )
        }

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3F))

        Column(
            modifier.fillMaxWidth().padding(vertical = 12.dp),
        ) {
            content()
        }
    }
}
