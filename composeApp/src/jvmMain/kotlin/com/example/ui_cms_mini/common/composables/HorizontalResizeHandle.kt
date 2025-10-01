package com.example.ui_cms_mini.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.skiko.Cursor

@Composable
fun HorizontalResizeHandle(
    color: Color,
    onDrag: (Dp) -> Unit
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .height(10.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val delta = with(density) { dragAmount.y.toDp() }
                    onDrag(delta)
                }
            }
            .background(color = color)
            .cursorForVerticalResize()
    ) {
        // Línea central decorativa
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(20.dp)
                .align(Alignment.Center)
                .background(
                    color = Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        // Líneas decorativas arriba y abajo
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .background(Color.Gray.copy(alpha = 0.2f))
        )

        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(Color.Gray.copy(alpha = 0.2f))
        )
    }
}

@Composable
private fun Modifier.cursorForVerticalResize(): Modifier {
    return if (LocalInspectionMode.current) this else this.pointerHoverIcon(
        PointerIcon(
            Cursor(Cursor.N_RESIZE_CURSOR)
        )
    )
}
