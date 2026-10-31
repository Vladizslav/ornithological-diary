package com.example.ornithologicaldiary.ui.screens.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ornithologicaldiary.data.SampleData
import com.example.ornithologicaldiary.data.model.Species
import com.example.ornithologicaldiary.ui.components.AppTopBar

@Composable
fun GuideScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val found = SampleData.species.filter { species ->
        species.name.contains(query, ignoreCase = true) ||
            species.latinName.contains(query, ignoreCase = true)
    }

    Column(modifier = modifier.fillMaxSize()) {
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
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(found) { species ->
                SpeciesCard(species = species)
            }
        }
    }
}

@Composable
private fun SpeciesCard(species: Species) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
    }
}
