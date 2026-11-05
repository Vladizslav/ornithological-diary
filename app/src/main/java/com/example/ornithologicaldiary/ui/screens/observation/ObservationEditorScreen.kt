package com.example.ornithologicaldiary.ui.screens.observation

import android.content.ActivityNotFoundException
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity
import com.example.ornithologicaldiary.ui.components.AppTopBar
import com.example.ornithologicaldiary.util.DateFormat
import com.example.ornithologicaldiary.util.PhotoStorage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObservationEditorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ObservationEditorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    var pendingCameraPath by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.onPhotoChange(pendingCameraPath)
        } else {
            PhotoStorage.delete(pendingCameraPath)
            pendingCameraPath = null
        }
    }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onPhotoPicked(uri)
        }
    }

    LaunchedEffect(state.finished) {
        if (state.finished) onBack()
    }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = if (state.isNew) "Новое наблюдение" else "Наблюдение",
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
            SpeciesSelector(
                options = state.speciesOptions,
                selectedId = state.speciesId,
                onSelect = viewModel::onSpeciesSelected
            )

            if (state.speciesOptions.isEmpty()) {
                Text(
                    text = "В справочнике нет видов — добавьте их на вкладке «Справочник».",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = DateFormat.format(state.observedAt),
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    label = { Text(text = "Дата") },
                    readOnly = true,
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Выбрать дату"
                    )
                }
            }

            OutlinedTextField(
                value = state.place,
                onValueChange = viewModel::onPlaceChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Место") },
                singleLine = true
            )

            OutlinedTextField(
                value = state.note,
                onValueChange = viewModel::onNoteChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Заметка") },
                minLines = 3
            )

            Text(
                text = "Фотография",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            val photoPath = state.photoPath
            if (photoPath == null) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = {
                            val file = PhotoStorage.createFile(context)
                            pendingCameraPath = file.absolutePath
                            try {
                                cameraLauncher.launch(PhotoStorage.contentUri(context, file))
                            } catch (exception: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    "На устройстве нет приложения камеры",
                                    Toast.LENGTH_SHORT
                                ).show()
                                PhotoStorage.delete(file.absolutePath)
                                pendingCameraPath = null
                            }
                        }
                    ) {
                        Text(text = "Сделать фото")
                    }
                    OutlinedButton(
                        onClick = {
                            pickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Text(text = "Выбрать из галереи")
                    }
                }
            } else {
                AsyncImage(
                    model = File(photoPath),
                    contentDescription = "Фотография наблюдения",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                TextButton(onClick = { viewModel.onPhotoChange(null) }) {
                    Text(text = "Удалить фото")
                }
            }

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.loaded && state.canSave
            ) {
                Text(text = "Сохранить")
            }
        }
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = state.observedAt)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let(viewModel::onDateChange)
                        showDatePicker = false
                    }
                ) {
                    Text(text = "ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = "Отмена")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpeciesSelector(
    options: List<SpeciesEntity>,
    selectedId: Long,
    onSelect: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = options.firstOrNull { it.id == selectedId }?.name ?: "Выберите вид"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            label = { Text(text = "Вид птицы") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { species ->
                DropdownMenuItem(
                    text = { Text(text = species.name) },
                    onClick = {
                        onSelect(species.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
