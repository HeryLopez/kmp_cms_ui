package com.example.ui_cms_mini.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui_cms_mini.common.composables.CustomIconButton

@Composable
fun ContainerHeader(
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemoveClick: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1F),
                text = "Container",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8F)
                )
            )

            CustomIconButton(
                resource = Icons.Outlined.ArrowUpward,
                contentDescription = "Move up",
                onClick = onMoveUp,
            )

            CustomIconButton(
                resource = Icons.Outlined.ArrowDownward,
                onClick = onMoveDown,
                contentDescription = "Move down",
            )

            CustomIconButton(
                resource = Icons.Outlined.Delete,
                onClick = { showConfirmDialog = true },
                contentDescription = "Delete",
            )
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "Delete Container",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete this container? All components inside it will also be removed."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onRemoveClick()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}