package com.example.ornithologicaldiary.ui.screens.diary

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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ornithologicaldiary.data.SampleData
import com.example.ornithologicaldiary.data.model.Observation
import com.example.ornithologicaldiary.ui.components.AppTopBar

@Composable
fun DiaryScreen(
    onAddObservation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(title = "Дневник")
            Text(
                text = "Макет экрана: ниже пример записей, которые появятся из базы данных.",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(SampleData.observations) { observation ->
                    ObservationCard(observation = observation)
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
}

@Composable
private fun ObservationCard(observation: Observation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = observation.species,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = observation.date,
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
                    text = observation.place,
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (observation.note.isNotBlank()) {
                Text(
                    text = observation.note,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
