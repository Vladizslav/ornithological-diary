package com.example.ornithologicaldiary.ui.screens.observation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ornithologicaldiary.data.local.AppDatabase
import com.example.ornithologicaldiary.data.local.ObservationWithSpecies
import com.example.ornithologicaldiary.data.repository.ObservationRepository
import com.example.ornithologicaldiary.navigation.Routes
import com.example.ornithologicaldiary.util.PhotoStorage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ObservationViewState(
    val item: ObservationWithSpecies? = null,
    val loaded: Boolean = false
)

class ObservationViewViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = ObservationRepository(AppDatabase.getInstance(application))

    private val observationId: Long = savedStateHandle[Routes.ARG_ID] ?: Routes.NEW_ID

    val state: StateFlow<ObservationViewState> = repository.observeById(observationId)
        .map { ObservationViewState(item = it, loaded = true) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ObservationViewState()
        )

    fun delete() {
        val item = state.value.item ?: return
        viewModelScope.launch {
            PhotoStorage.delete(item.observation.photoPath)
            repository.deleteById(item.observation.id)
        }
    }
}