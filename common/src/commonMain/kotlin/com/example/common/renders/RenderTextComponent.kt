package com.example.common.renders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.model.TextComponent

@Composable
fun RenderTextComponent(component: TextComponent) {
    val backgroundColor = component.backgroundColorValue ?: Color.Transparent
    val textColor = component.textColorValue ?: Color.Black

    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor,
        fontSize = component.fontSizeValue,
        fontWeight = component.fontWeightValue,
        fontStyle = component.fontStyleValue,
        textAlign = component.textAlignValue,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = component.text,
            style = textStyle
        )
    }
}