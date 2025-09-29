package com.example.ui_cms_mini.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
fun VerticalResizeHandle(
    color: Color,
    onDrag: (Dp) -> Unit
) {
    val density = LocalDensity.current


    Box(
        modifier = Modifier
            .width(10.dp)
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val delta = with(density) { dragAmount.x.toDp() }
                    onDrag(delta)
                }
            }
            .background(color = color)
            .cursorForHorizontalResize()
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(20.dp)
                .align(Alignment.Center)
                .background(
                    color = Color.Gray.copy(alpha = 0.5F),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(
                    color = Color.Gray.copy(alpha = 0.2F),
                )
        )

        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .background(
                    color = Color.Gray.copy(alpha = 0.2F),
                )
        )
    }
}


@Composable
private fun Modifier.cursorForHorizontalResize(): Modifier {
    return if (LocalInspectionMode.current) this else this.pointerHoverIcon(
        PointerIcon(
            Cursor(
                Cursor.E_RESIZE_CURSOR
            )
        )
    )
}

