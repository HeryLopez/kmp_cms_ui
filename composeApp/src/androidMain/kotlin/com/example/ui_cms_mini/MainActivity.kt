package com.example.ui_cms_mini

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.RenderScreen
import com.example.common.repository.ComponentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .systemBarsPadding()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ContentComposable()
                }
            }
        }
    }
}

@Composable
fun ContentComposable() {
    val repo = ComponentRepository("http://10.0.2.2:9090")
    var jsonList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Función para cargar datos
    suspend fun loadData() {
        isLoading = true
        try {
            delay(1000)
            jsonList = repo.getAll()
        } catch (e: Exception) {
            Log.e("ContentComposable", "Error fetching data", e)
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RenderScreen(jsonList)
        }

        FloatingActionButton(
            onClick = {
                scope.launch {
                    loadData()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // posición flotante
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(Icons.Filled.Refresh, "Floating action button.")
            }
        }
    }
}
