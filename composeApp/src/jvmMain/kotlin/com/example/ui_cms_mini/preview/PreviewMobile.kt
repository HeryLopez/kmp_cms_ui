package com.example.ui_cms_mini.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.renders.RenderScreen
import com.example.ui_cms_mini.ListViewModel

@Composable
fun PreviewMobile(viewModel: ListViewModel) {
    val jsonExport by viewModel.jsonExport.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .width(400.dp)
            .height(780.dp)
            .padding(6.dp)
            .background(Color.Black, RoundedCornerShape(40.dp))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White, RoundedCornerShape(20.dp)).verticalScroll(scrollState)
        ) {
            RenderScreen(jsonExport)
        }
    }
}
