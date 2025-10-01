package com.example.ui_cms_mini.components.imageBlock

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.example.common.model.ComponentType
import org.jetbrains.compose.resources.painterResource
import ui_cms_mini.composeapp.generated.resources.Res
import ui_cms_mini.composeapp.generated.resources.delete_icon
import ui_cms_mini.composeapp.generated.resources.image_icon

@Composable
fun ImageBlockThumbnail() {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                ComponentType.IMAGE_BLOCK.title,
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(8.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
               // contentAlignment = Alignment.Center
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Placeholder de imagen
                Image(
                    painter = painterResource(Res.drawable.image_icon,),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Gray),
                    modifier = Modifier
                        .height(40.dp)
                        .width(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                // Skeleton
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(10.dp)
                        .background(Color.Gray, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}