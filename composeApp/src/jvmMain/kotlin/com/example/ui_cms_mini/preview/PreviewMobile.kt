package com.example.ui_cms_mini.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.RenderScreen
import com.example.ui_cms_mini.listComponents.ListViewModel

@Composable
fun PreviewMobile(viewModel: ListViewModel) {
    val jsonExport by viewModel.jsonExport.collectAsState()

    Box(
        modifier = Modifier
            .width(400.dp)   // ancho aproximado de un móvil
            .height(800.dp)  // alto aproximado de un móvil
            .background(Color.Black, RoundedCornerShape(40.dp)) // borde exterior tipo móvil
            .padding(16.dp)  // margen interior para simular el bezel
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White, RoundedCornerShape(20.dp)) // pantalla del móvil
        ) {
            RenderScreen(jsonExport)
        }
    }
}
