package com.example.ui_cms_mini.components.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun TextProp(label: String, text: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            ) {
                append(label)
                append(" ")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 10.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            ) {
                append(text)
            }
        },
        style = MaterialTheme.typography.bodySmall
    )

}