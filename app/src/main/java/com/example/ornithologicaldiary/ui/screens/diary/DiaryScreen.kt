package com.example.ornithologicaldiary.ui.screens.diary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ornithologicaldiary.data.local.ObservationWithSpecies
import com.example.ornithologicaldiary.ui.components.AppTopBar
import com.example.ornithologicaldiary.ui.components.ConfirmDeleteDialog
import com.example.ornithologicaldiary.ui.components.EmptyState
import com.example.ornithologicaldiary.util.DateFormat

@Composable
fun DiaryScreen(
    onAddObservation: () -> Unit,
    onEditObservation: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DiaryViewModel = viewModel()
) {
    val observations by viewModel.observations.collectAsStateWithLifecycle()
    var observationToDelete by remember { mutableStateOf<ObservationWithSpecies?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(title = "Дневник")
            if (observations.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.Today,
                    title = "Пока нет наблюдений",
                    description = "Нажмите «Наблюдение», чтобы добавить первую запись."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(observations, key = { it.observation.id }) { item ->
                        ObservationCard(
                            item = item,
                            onClick = { onEditObservation(item.observation.id) },
                            onDelete = { observationToDelete = item }
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onAddObservation,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
            text = { Text(text = "Наблюдение") }
        )
    }

    observationToDelete?.let { item ->
        ConfirmDeleteDialog(
            title = "Удалить наблюдение?",
            message = "Запись «${item.speciesName}» будет удалена безвозвратно.",
            onConfirm = {
                viewModel.delete(item.observation.id)
                observationToDelete = null
            },
            onDismiss = { observationToDelete = null }
        )
    }
}

@Composable
private fun ObservationCard(
    item: ObservationWithSpecies,
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
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.speciesName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = DateFormat.format(item.observation.observedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.observation.place,
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (item.observation.note.isNotBlank()) {
                    Text(
                        text = item.observation.note,
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
