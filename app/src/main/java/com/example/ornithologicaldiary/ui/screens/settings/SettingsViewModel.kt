package com.example.ornithologicaldiary.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ornithologicaldiary.data.preferences.SettingsRepository
import com.example.ornithologicaldiary.data.preferences.SortOrder
import com.example.ornithologicaldiary.data.preferences.ThemeMode
import com.example.ornithologicaldiary.data.preferences.UserSettings
import com.example.ornithologicaldiary.notification.ReminderScheduler
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository.getInstance(application)

    val settings: StateFlow<UserSettings> = repository.settings

    init {
        if (repository.settings.value.dailyReminder) {
            ReminderScheduler.schedule(application)
        }
    }

    fun setThemeMode(mode: ThemeMode) = repository.setThemeMode(mode)

    fun setSortOrder(order: SortOrder) = repository.setSortOrder(order)

    fun setShowNotes(value: Boolean) = repository.setShowNotes(value)

    fun setDailyReminder(enabled: Boolean) {
        repository.setDailyReminder(enabled)
        val context = getApplication<Application>()
        if (enabled) {
            ReminderScheduler.schedule(context)
        } else {
            ReminderScheduler.cancel(context)
        }
    }

    fun showNotificationNow() = ReminderScheduler.showNow(getApplication())
}
