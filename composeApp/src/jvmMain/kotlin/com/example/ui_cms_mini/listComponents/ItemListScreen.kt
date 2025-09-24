package com.example.ui_cms_mini.listComponents
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


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


@Composable
fun ItemListScreen(viewModel: ListViewModel) {
    val items by viewModel.items.collectAsState()
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(item.colorColor)
                    .padding(8.dp)
            ) {
                Text(item.text, color = Color.White)
            }
        }
    }
}



