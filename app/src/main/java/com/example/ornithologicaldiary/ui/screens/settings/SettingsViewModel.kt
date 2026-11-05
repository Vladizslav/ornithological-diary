package com.example.ornithologicaldiary.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ornithologicaldiary.data.preferences.SettingsRepository
import com.example.ornithologicaldiary.data.preferences.SortOrder
import com.example.ornithologicaldiary.data.preferences.ThemeMode
import com.example.ornithologicaldiary.data.preferences.UserSettings
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository.getInstance(application)

    val settings: StateFlow<UserSettings> = repository.settings

    fun setThemeMode(mode: ThemeMode) = repository.setThemeMode(mode)

    fun setSortOrder(order: SortOrder) = repository.setSortOrder(order)

    fun setShowNotes(value: Boolean) = repository.setShowNotes(value)
}
