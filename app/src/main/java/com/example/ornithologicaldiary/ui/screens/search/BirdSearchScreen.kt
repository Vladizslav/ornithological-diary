package com.example.ornithologicaldiary.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ornithologicaldiary.R
import com.example.ornithologicaldiary.data.remote.dto.TaxonDto
import com.example.ornithologicaldiary.ui.components.AppTopBar
import com.example.ornithologicaldiary.ui.components.EmptyState

@Composable
fun BirdSearchScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BirdSearchViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.search_title), onBack = onBack)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { viewModel.search(query) },
                enabled = query.isNotBlank() && state != BirdSearchState.Loading
            ) {
                Text(text = stringResource(R.string.search_button))
            }
        }

        when (val current = state) {
            BirdSearchState.Idle -> EmptyState(
                icon = Icons.Filled.Public,
                title = stringResource(R.string.search_idle_title),
                description = stringResource(R.string.search_idle_description)
            )

            BirdSearchState.Loading -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is BirdSearchState.Error -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EmptyState(
                    icon = Icons.Filled.CloudOff,
                    title = stringResource(R.string.search_error_title),
                    description = current.message
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = viewModel::retry) {
                    Text(text = stringResource(R.string.common_retry))
                }
            }

            is BirdSearchState.Success -> {
                if (current.birds.isEmpty()) {
                    EmptyState(
                        icon = Icons.Filled.SearchOff,
                        title = stringResource(R.string.search_empty_title),
                        description = stringResource(R.string.search_empty_description)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(current.birds, key = { it.id }) { bird ->
                            BirdCard(bird = bird)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BirdCard(bird: TaxonDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = bird.commonName.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = bird.latinName.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.search_observations_count, bird.observationsCount),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
