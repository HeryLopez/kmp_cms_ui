package com.example.ui_cms_mini.components.textBlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.common.model.TextComponent
import com.example.common.renders.RenderTextComponent

@Composable
fun TextBlockDropComponent(component: TextComponent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        RenderTextComponent(component)
    }
}