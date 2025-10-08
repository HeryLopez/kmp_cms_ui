package com.example.ui_cms_mini.properties.common

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.utils.ColorUtils
import com.example.common.utils.textColorForContrast
import java.awt.Color as AwtColor
import javax.swing.JColorChooser

@Composable
fun ColorPickerProp(
    label: String,
    initialColor: String?,
    onColorSelected: (String?) -> Unit
) {
    var currentColor by remember(initialColor) {
        mutableStateOf(
            ColorUtils.hexToColor(initialColor) ?: Color.Transparent
        )
    }

    Column(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            label,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .clickable {
                        val awtColor = JColorChooser.showDialog(
                            null,
                            label,
                            AwtColor(
                                currentColor.red,
                                currentColor.green,
                                currentColor.blue,
                                currentColor.alpha
                            )
                        )
                        if (awtColor != null) {
                            currentColor =
                                Color(awtColor.red, awtColor.green, awtColor.blue, awtColor.alpha)
                            onColorSelected(
                                "#%02X%02X%02X".format(
                                    awtColor.red,
                                    awtColor.green,
                                    awtColor.blue
                                )
                            )
                        }
                    }
                    .background(currentColor, shape = RoundedCornerShape(4.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 12.dp),

                contentAlignment = Alignment.Center
            ) {
                if (initialColor == null) {
                    Text(
                        "Transparent",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    )
                } else {
                    Text(
                        initialColor,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentColor.textColorForContrast()
                        )
                    )
                }
            }

            if (initialColor != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            onClick = {
                                onColorSelected(null)
                            }
                        )
                        .padding(4.dp)
                ) {
                    Text(
                        "Remove color",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }


    }
}