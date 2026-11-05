package com.example.ornithologicaldiary.ui.screens.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ornithologicaldiary.data.local.AppDatabase
import com.example.ornithologicaldiary.data.local.ObservationWithSpecies
import com.example.ornithologicaldiary.data.preferences.SettingsRepository
import com.example.ornithologicaldiary.data.preferences.SortOrder
import com.example.ornithologicaldiary.data.preferences.UserSettings
import com.example.ornithologicaldiary.data.repository.ObservationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ObservationRepository(AppDatabase.getInstance(application))
    private val settingsRepository = SettingsRepository.getInstance(application)

    val settings: StateFlow<UserSettings> = settingsRepository.settings

    val observations: StateFlow<List<ObservationWithSpecies>> =
        combine(repository.observeAll(), settingsRepository.settings) { observations, settings ->
            when (settings.sortOrder) {
                SortOrder.DATE_DESC -> observations.sortedByDescending { it.observation.observedAt }
                SortOrder.SPECIES_ASC -> observations.sortedBy { it.speciesName.lowercase() }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
