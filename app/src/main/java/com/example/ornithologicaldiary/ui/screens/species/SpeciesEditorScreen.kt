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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ornithologicaldiary.R
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
            title = stringResource(
                if (state.isNew) R.string.species_new_title else R.string.species_edit_title
            ),
            onBack = onBack,
            actions = {
                if (!state.isNew) {
                    IconButton(onClick = viewModel::delete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.species_delete_description)
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
                label = { Text(text = stringResource(R.string.species_name_label)) },
                placeholder = { Text(text = stringResource(R.string.species_name_placeholder)) },
                singleLine = true
            )
            OutlinedTextField(
                value = state.latinName,
                onValueChange = viewModel::onLatinNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.species_latin_label)) },
                placeholder = { Text(text = stringResource(R.string.species_latin_placeholder)) },
                singleLine = true
            )
            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.loaded && state.canSave
            ) {
                Text(text = stringResource(R.string.common_save))
            }
        }
    }
}
