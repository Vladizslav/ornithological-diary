package com.example.ornithologicaldiary.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ornithologicaldiary.ui.components.AppTopBar
import com.example.ornithologicaldiary.ui.components.FlyingBird

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = "Карта")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlyingBird(modifier = Modifier.size(200.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Карта наблюдений",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Здесь будут отмечены места встреч с птицами. " +
                        "Экран пока пустой — карта появится на следующем этапе.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
