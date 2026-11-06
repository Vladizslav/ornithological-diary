package com.example.ornithologicaldiary.ui.screens.observation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ornithologicaldiary.R
import com.example.ornithologicaldiary.ui.components.AppTopBar
import com.example.ornithologicaldiary.ui.components.ConfirmDeleteDialog
import com.example.ornithologicaldiary.util.DateFormat
import java.io.File

@Composable
fun ObservationViewScreen(
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ObservationViewViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDelete by remember { mutableStateOf(false) }

    LaunchedEffect(state.loaded, state.item) {
        if (state.loaded && state.item == null) onBack()
    }

    val item = state.item

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = stringResource(R.string.observation_view_title),
            onBack = onBack,
            actions = {
                if (item != null) {
                    IconButton(onClick = { onEdit(item.observation.id) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.observation_edit_action)
                        )
                    }
                    IconButton(onClick = { showDelete = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.observation_delete_description),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        )

        if (item != null) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item.observation.photoPath?.let { path ->
                    AsyncImage(
                        model = File(path),
                        contentDescription = stringResource(R.string.observation_photo_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = item.speciesName,
                    style = MaterialTheme.typography.headlineSmall
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = DateFormat.format(item.observation.observedAt),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.observation.place,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (item.observation.note.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.observation_note_label),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.observation.note,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    if (showDelete) {
        ConfirmDeleteDialog(
            title = stringResource(R.string.diary_delete_title),
            message = stringResource(R.string.diary_delete_message, item?.speciesName.orEmpty()),
            onConfirm = {
                showDelete = false
                viewModel.delete()
            },
            onDismiss = { showDelete = false }
        )
    }
}