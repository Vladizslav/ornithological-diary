package com.example.ornithologicaldiary.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ornithologicaldiary.data.preferences.SortOrder
import com.example.ornithologicaldiary.data.preferences.ThemeMode
import com.example.ornithologicaldiary.ui.components.AppTopBar

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = "Настройки")

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SectionTitle(text = "Тема оформления")
            ThemeMode.entries.forEach { mode ->
                SettingRow(
                    label = mode.label,
                    selected = settings.themeMode == mode,
                    onClick = { viewModel.setThemeMode(mode) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = "Сортировка наблюдений")
            SortOrder.entries.forEach { order ->
                SettingRow(
                    label = order.label,
                    selected = settings.sortOrder == order,
                    onClick = { viewModel.setSortOrder(order) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = "Отображение")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Показывать заметки",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Заметка отображается в карточке наблюдения",
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
            SectionTitle(text = "Уведомления")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ежедневное напоминание",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Каждый день в 19:00",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = settings.dailyReminder,
                    onCheckedChange = viewModel::setDailyReminder
                )
            }
            TextButton(onClick = viewModel::showNotificationNow) {
                Text(text = "Показать уведомление сейчас")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Настройки сохраняются на устройстве и остаются после перезапуска приложения.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
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
