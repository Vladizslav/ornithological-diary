package com.example.ornithologicaldiary.ui.screens.observation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ornithologicaldiary.ui.components.AppTopBar

@Composable
fun AddObservationScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = "Новое наблюдение", onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Форма-заглушка: поля появятся в рабочем виде на следующем этапе.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Вид птицы") },
                placeholder = { Text(text = "Например, большая синица") },
                enabled = false,
                singleLine = true
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Дата и время") },
                enabled = false,
                singleLine = true
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Место") },
                enabled = false,
                singleLine = true
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Заметка") },
                enabled = false,
                minLines = 3
            )
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            ) {
                Text(text = "Сохранить")
            }
        }
    }
}
