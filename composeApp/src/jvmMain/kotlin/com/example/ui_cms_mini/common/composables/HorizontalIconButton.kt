package com.example.ui_cms_mini.common.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HorizontalIconButton(
    resource: ImageVector,
    iconColor: Color = Color.Black,
    text: String? = null,
    textColor: Color = Color.Black,
    rippleRounded: Dp = 32.dp,
    expand: Boolean = false,
    iconSize: Dp = 20.dp,
    onClick: () -> Unit
) {
    val shape = when (rippleRounded) {
        0.dp -> RoundedCornerShape(0.dp)
        50.dp -> CircleShape
        else -> RoundedCornerShape(rippleRounded)
    }

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .padding(6.dp)
            .then(
                if (expand) Modifier.fillMaxWidth() else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = resource,
            contentDescription = "Home icon outlined",
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(color = textColor, fontSize = 10.sp)
            )
        }
    }
}
