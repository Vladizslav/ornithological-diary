package com.example.ornithologicaldiary.ui.screens.guide

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity
import com.example.ornithologicaldiary.ui.components.AppTopBar
import com.example.ornithologicaldiary.ui.components.ConfirmDeleteDialog
import com.example.ornithologicaldiary.ui.components.EmptyState

@Composable
fun GuideScreen(
    onAddSpecies: () -> Unit,
    onEditSpecies: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideViewModel = viewModel()
) {
    val species by viewModel.species.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    var speciesToDelete by remember { mutableStateOf<SpeciesEntity?>(null) }

    val found = species.filter { item ->
        item.name.contains(query, ignoreCase = true) ||
            item.latinName.contains(query, ignoreCase = true)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(title = "Справочник")
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(text = "Поиск по названию") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                singleLine = true
            )

            if (found.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.MenuBook,
                    title = "Список пуст",
                    description = "Добавьте первый вид птицы кнопкой «Вид»."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(found, key = { it.id }) { item ->
                        SpeciesCard(
                            species = item,
                            onClick = { onEditSpecies(item.id) },
                            onDelete = { speciesToDelete = item }
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onAddSpecies,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
            text = { Text(text = "Вид") }
        )
    }

    speciesToDelete?.let { item ->
        ConfirmDeleteDialog(
            title = "Удалить вид?",
            message = "«${item.name}» и все наблюдения этого вида будут удалены.",
            onConfirm = {
                viewModel.delete(item)
                speciesToDelete = null
            },
            onDismiss = { speciesToDelete = null }
        )
    }
}

@Composable
private fun SpeciesCard(
    species: SpeciesEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = species.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = species.latinName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
