package com.example.ui_cms_mini.components.randomText

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.common.model.TextComponent

@Composable
fun TextBlockDropComponent(component: TextComponent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Text: ${component.text}")
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "Color: ${component.color}")
            Box(
                modifier = Modifier.height(16.dp).width(16.dp).background(component.colorColor)
            )
        }

    }
}