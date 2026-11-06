package com.example.ornithologicaldiary.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository private constructor(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(read())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        _settings.value = read()
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun setThemeMode(mode: ThemeMode) {
        preferences.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }

    fun setSortOrder(order: SortOrder) {
        preferences.edit().putString(KEY_SORT_ORDER, order.name).apply()
    }

    fun setShowNotes(value: Boolean) {
        preferences.edit().putBoolean(KEY_SHOW_NOTES, value).apply()
    }

    fun setDailyReminder(value: Boolean) {
        preferences.edit().putBoolean(KEY_DAILY_REMINDER, value).apply()
    }

    fun setReminderTime(hour: Int, minute: Int) {
        preferences.edit()
            .putInt(KEY_REMINDER_HOUR, hour)
            .putInt(KEY_REMINDER_MINUTE, minute)
            .apply()
    }

    private fun read(): UserSettings = UserSettings(
        themeMode = ThemeMode.entries
            .firstOrNull { it.name == preferences.getString(KEY_THEME_MODE, null) }
            ?: ThemeMode.SYSTEM,
        sortOrder = SortOrder.entries
            .firstOrNull { it.name == preferences.getString(KEY_SORT_ORDER, null) }
            ?: SortOrder.DATE_DESC,
        showNotes = preferences.getBoolean(KEY_SHOW_NOTES, true),
        dailyReminder = preferences.getBoolean(KEY_DAILY_REMINDER, false),
        reminderHour = preferences.getInt(KEY_REMINDER_HOUR, 19),
        reminderMinute = preferences.getInt(KEY_REMINDER_MINUTE, 0)
    )

    companion object {
        private const val PREFS_NAME = "user_settings"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_SORT_ORDER = "sort_order"
        private const val KEY_SHOW_NOTES = "show_notes"
        private const val KEY_DAILY_REMINDER = "daily_reminder"
        private const val KEY_REMINDER_HOUR = "reminder_hour"
        private const val KEY_REMINDER_MINUTE = "reminder_minute"

        @Volatile
        private var instance: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository =
            instance ?: synchronized(this) {
                instance ?: SettingsRepository(context.applicationContext).also { instance = it }
            }
    }
}
