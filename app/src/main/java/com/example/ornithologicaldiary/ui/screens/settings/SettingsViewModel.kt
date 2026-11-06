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
        val current = repository.settings.value
        if (current.dailyReminder) {
            ReminderScheduler.schedule(application, current.reminderHour, current.reminderMinute)
        }
    }

    fun setThemeMode(mode: ThemeMode) = repository.setThemeMode(mode)

    fun setSortOrder(order: SortOrder) = repository.setSortOrder(order)

    fun setShowNotes(value: Boolean) = repository.setShowNotes(value)

    fun setDailyReminder(enabled: Boolean) {
        repository.setDailyReminder(enabled)
        val current = repository.settings.value
        val context = getApplication<Application>()
        if (enabled) {
            ReminderScheduler.schedule(context, current.reminderHour, current.reminderMinute)
        } else {
            ReminderScheduler.cancel(context)
        }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        repository.setReminderTime(hour, minute)
        val current = repository.settings.value
        if (current.dailyReminder) {
            ReminderScheduler.schedule(getApplication(), hour, minute)
        }
    }

    fun showNotificationNow() = ReminderScheduler.showNow(getApplication())
}
