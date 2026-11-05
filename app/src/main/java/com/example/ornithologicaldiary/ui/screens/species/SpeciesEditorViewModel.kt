package com.example.ornithologicaldiary.ui.screens.species

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ornithologicaldiary.data.local.AppDatabase
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity
import com.example.ornithologicaldiary.data.repository.ObservationRepository
import com.example.ornithologicaldiary.data.repository.SpeciesRepository
import com.example.ornithologicaldiary.navigation.Routes
import com.example.ornithologicaldiary.util.PhotoStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpeciesEditorState(
    val id: Long = Routes.NEW_ID,
    val name: String = "",
    val latinName: String = "",
    val isNew: Boolean = true,
    val loaded: Boolean = false,
    val finished: Boolean = false
) {
    val canSave: Boolean get() = name.isNotBlank()
}

class SpeciesEditorViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = SpeciesRepository(database)
    private val observationRepository = ObservationRepository(database)

    private val speciesId: Long = savedStateHandle[Routes.ARG_ID] ?: Routes.NEW_ID

    private val _state = MutableStateFlow(SpeciesEditorState())
    val state: StateFlow<SpeciesEditorState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val existing = if (speciesId == Routes.NEW_ID) null else repository.findById(speciesId)
            _state.update {
                it.copy(
                    id = existing?.id ?: Routes.NEW_ID,
                    name = existing?.name.orEmpty(),
                    latinName = existing?.latinName.orEmpty(),
                    isNew = existing == null,
                    loaded = true
                )
            }
        }
    }

    fun onNameChange(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun onLatinNameChange(value: String) {
        _state.update { it.copy(latinName = value) }
    }

    fun save() {
        val current = _state.value
        if (!current.canSave) return
        viewModelScope.launch {
            repository.save(
                SpeciesEntity(
                    id = if (current.isNew) 0L else current.id,
                    name = current.name.trim(),
                    latinName = current.latinName.trim()
                )
            )
            _state.update { it.copy(finished = true) }
        }
    }

    fun delete() {
        val current = _state.value
        if (current.isNew) return
        viewModelScope.launch {
            observationRepository.getBySpeciesId(current.id).forEach { observation ->
                PhotoStorage.delete(observation.photoPath)
            }
            repository.delete(
                SpeciesEntity(
                    id = current.id,
                    name = current.name,
                    latinName = current.latinName
                )
            )
            _state.update { it.copy(finished = true) }
        }
    }
}
