package com.example.common.utils

import androidx.compose.ui.graphics.Color

fun Color.textColorForContrast(): Color {
    val luminance = (0.299 * red + 0.587 * green + 0.114 * blue)
    return if (luminance > 0.5) Color.Black else Color.White
}

fun Color.toHex(): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    return "#${r.toString(16).padStart(2, '0').uppercase()}${g.toString(16).padStart(2, '0').uppercase()}${b.toString(16).padStart(2, '0').uppercase()}"
}
