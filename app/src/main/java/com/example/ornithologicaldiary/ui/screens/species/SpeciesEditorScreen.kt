package com.example.ornithologicaldiary.ui.screens.species

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ornithologicaldiary.ui.components.AppTopBar

@Composable
fun SpeciesEditorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeciesEditorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.finished) {
        if (state.finished) onBack()
    }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = if (state.isNew) "Новый вид" else "Вид птицы",
            onBack = onBack,
            actions = {
                if (!state.isNew) {
                    IconButton(onClick = viewModel::delete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Удалить"
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Название") },
                placeholder = { Text(text = "Например, большая синица") },
                singleLine = true
            )
            OutlinedTextField(
                value = state.latinName,
                onValueChange = viewModel::onLatinNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Латинское название") },
                placeholder = { Text(text = "Parus major") },
                singleLine = true
            )
            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.loaded && state.canSave
            ) {
                Text(text = "Сохранить")
            }
        }
    }
}
