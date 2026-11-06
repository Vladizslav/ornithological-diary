package com.example.ornithologicaldiary.ui.screens.settings

import android.widget.NumberPicker
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ornithologicaldiary.R
import com.example.ornithologicaldiary.data.preferences.SortOrder
import com.example.ornithologicaldiary.data.preferences.ThemeMode
import com.example.ornithologicaldiary.ui.components.AppTopBar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    var showTimePicker by remember { mutableStateOf(false) }
    val timeText = String.format(Locale.getDefault(), "%02d:%02d", settings.reminderHour, settings.reminderMinute)

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.settings_title))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SectionTitle(text = stringResource(R.string.settings_section_theme))
            ThemeMode.entries.forEach { mode ->
                SettingRow(
                    label = stringResource(mode.labelRes),
                    selected = settings.themeMode == mode,
                    onClick = { viewModel.setThemeMode(mode) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = stringResource(R.string.settings_section_sort))
            SortOrder.entries.forEach { order ->
                SettingRow(
                    label = stringResource(order.labelRes),
                    selected = settings.sortOrder == order,
                    onClick = { viewModel.setSortOrder(order) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = stringResource(R.string.settings_section_display))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.settings_show_notes_title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.settings_show_notes_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = settings.showNotes,
                    onCheckedChange = viewModel::setShowNotes
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = stringResource(R.string.settings_section_notifications))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.settings_daily_reminder_title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.settings_daily_reminder_description, timeText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = settings.dailyReminder,
                    onCheckedChange = viewModel::setDailyReminder
                )
            }
            TextButton(onClick = { showTimePicker = true }) {
                Text(text = stringResource(R.string.settings_change_time))
            }
            TextButton(onClick = viewModel::showNotificationNow) {
                Text(text = stringResource(R.string.settings_show_notification))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.settings_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showTimePicker) {
        var hour by remember { mutableIntStateOf(settings.reminderHour) }
        var minute by remember { mutableIntStateOf(settings.reminderMinute) }
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text(text = stringResource(R.string.settings_reminder_time_title)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.setReminderTime(hour, minute)
                        showTimePicker = false
                    }
                ) {
                    Text(text = stringResource(R.string.common_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(text = stringResource(R.string.common_cancel))
                }
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PickerColumn(value = hour, range = 0..23, onValueChange = { hour = it })
                    Text(
                        text = ":",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    PickerColumn(value = minute, range = 0..59, onValueChange = { minute = it })
                }
            }
        )
    }
}

@Composable
private fun PickerColumn(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                wrapSelectorWheel = true
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                setFormatter { item -> String.format(Locale.getDefault(), "%02d", item) }
                setOnValueChangedListener { _, _, newValue -> onValueChange(newValue) }
            }
        },
        update = { picker ->
            if (picker.minValue != range.first) picker.minValue = range.first
            if (picker.maxValue != range.last) picker.maxValue = range.last
            if (picker.value != value) picker.value = value
        },
        modifier = Modifier
            .width(88.dp)
            .height(180.dp)
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(bottom = 4.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun SettingRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            text = label,
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}