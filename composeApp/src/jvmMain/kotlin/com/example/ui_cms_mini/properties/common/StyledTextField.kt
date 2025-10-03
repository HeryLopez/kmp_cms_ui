package com.example.ui_cms_mini.properties.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField(
    label: String,
    value: String,
    placeholder: String = "",
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            )
        )
        BasicTextField(
            value = value,
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = Color.Black
            ),
            onValueChange = onValueChange,
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth(),
            //.background(color = Color.White),
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    singleLine = true,
                    enabled = true,
                    placeholder = {
                        Text(
                            placeholder,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray
                            ),
                        )
                    },
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        errorBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray,
                    ),
                )
            }
        )
    }
}