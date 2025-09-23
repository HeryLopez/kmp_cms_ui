package com.example.ui_cms_mini

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val platform: String = "android"
}

actual fun getPlatform(): Platform = AndroidPlatform()