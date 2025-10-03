package com.example.common.utils

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun hexToColor(hex: String?): Color? {
        if (hex.isNullOrBlank()) return null

        val cleanHex = hex.removePrefix("#")

        // 6 for RGB, 8 for ARGB)
        if (cleanHex.length != 6 && cleanHex.length != 8) return null

        return try {
            val colorInt = cleanHex.toLong(16)
            val argb = if (cleanHex.length == 6) {
                // Add opacity
                colorInt or 0xFF000000
            } else {
                colorInt
            }
            Color(argb.toInt())
        } catch (e: NumberFormatException) {
            null
        }
    }

}