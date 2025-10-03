package com.example.ui_cms_mini.properties.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PropsTitle(title: String, id: String) {
    Column(
        modifier = Modifier.padding( 16.dp),
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            id,
            style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray, fontSize = 10.sp)
        )
    }
}