package com.example.common.utils

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun hexToColor(hex: String): Color {
        val cleanHex = hex.removePrefix("#")
        val colorInt = cleanHex.toLong(16)
        return Color((colorInt or 0xFF000000).toInt())
    }

}