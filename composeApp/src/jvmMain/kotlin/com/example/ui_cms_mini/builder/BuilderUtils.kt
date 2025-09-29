package com.example.ui_cms_mini.builder

object BuilderUtils {
    fun generateRandomText(): String {
        val emojis = listOf("🌟", "🚀", "🎉", "💡", "🔥", "🍀", "🌈", "✨")
        val messages = listOf(
            "Hello World!", "Keep Smiling!", "Stay Positive!", "Shine Bright!",
            "Good Vibes!", "Be Awesome!", "Enjoy Coding!", "Have Fun!"
        )

        val emoji = emojis.random()
        val message = messages.random()
        return "$emoji $message"
    }
}