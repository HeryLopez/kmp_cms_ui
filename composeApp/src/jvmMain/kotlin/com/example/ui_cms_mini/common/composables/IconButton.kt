package com.example.ui_cms_mini.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun IconButtonDesktop(
    resource: DrawableResource,
    iconColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(resource),
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier
                .height(20.dp)
                .width(20.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}
