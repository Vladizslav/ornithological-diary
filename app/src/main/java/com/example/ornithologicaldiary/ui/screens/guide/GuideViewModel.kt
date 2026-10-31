package com.example.ornithologicaldiary.ui.screens.guide

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ornithologicaldiary.data.local.AppDatabase
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity
import com.example.ornithologicaldiary.data.repository.SpeciesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GuideViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SpeciesRepository(AppDatabase.getInstance(application))

    val species: StateFlow<List<SpeciesEntity>> = repository.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun delete(species: SpeciesEntity) {
        viewModelScope.launch {
            repository.delete(species)
        }
    }
}
