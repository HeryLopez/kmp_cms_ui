package com.example.ui_cms_mini.components.imageBlock

import IconButtonDesktop
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
import com.example.common.model.ImageComponent
import ui_cms_mini.composeapp.generated.resources.Res
import ui_cms_mini.composeapp.generated.resources.delete_icon

@Composable
fun ImageBlockDropComponent(component: ImageComponent, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Title: ${component.title}")
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "Title Color: ${component.titleColor}")
                Box(
                    modifier = Modifier.height(16.dp).width(16.dp).background(component.titleColorColor)
                )
            }

        }
        Column(
            modifier = Modifier
        ) {
            IconButtonDesktop(
                resource = Res.drawable.delete_icon,
                onClick = onRemoveClick
            )
        }

    }
}