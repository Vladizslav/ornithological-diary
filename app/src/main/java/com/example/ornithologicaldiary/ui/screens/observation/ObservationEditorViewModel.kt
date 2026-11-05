package com.example.ornithologicaldiary.ui.screens.observation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ornithologicaldiary.data.local.AppDatabase
import com.example.ornithologicaldiary.data.local.entity.ObservationEntity
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity
import com.example.ornithologicaldiary.data.repository.ObservationRepository
import com.example.ornithologicaldiary.data.repository.SpeciesRepository
import com.example.ornithologicaldiary.navigation.Routes
import com.example.ornithologicaldiary.util.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ObservationEditorState(
    val id: Long = Routes.NEW_ID,
    val speciesId: Long = Routes.NEW_ID,
    val place: String = "",
    val observedAt: Long = System.currentTimeMillis(),
    val note: String = "",
    val photoPath: String? = null,
    val speciesOptions: List<SpeciesEntity> = emptyList(),
    val isNew: Boolean = true,
    val loaded: Boolean = false,
    val finished: Boolean = false
) {
    val canSave: Boolean get() = speciesId > 0 && place.isNotBlank()
}

class ObservationEditorViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val observationRepository = ObservationRepository(database)
    private val speciesRepository = SpeciesRepository(database)

    private val observationId: Long = savedStateHandle[Routes.ARG_ID] ?: Routes.NEW_ID

    private val _state = MutableStateFlow(ObservationEditorState())
    val state: StateFlow<ObservationEditorState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val species = speciesRepository.getAll()
            val existing = if (observationId == Routes.NEW_ID) {
                null
            } else {
                observationRepository.findById(observationId)
            }
            _state.update {
                it.copy(
                    id = existing?.id ?: Routes.NEW_ID,
                    speciesId = existing?.speciesId ?: species.firstOrNull()?.id ?: Routes.NEW_ID,
                    place = existing?.place.orEmpty(),
                    observedAt = existing?.observedAt ?: System.currentTimeMillis(),
                    note = existing?.note.orEmpty(),
                    photoPath = existing?.photoPath,
                    speciesOptions = species,
                    isNew = existing == null,
                    loaded = true
                )
            }
        }
    }

    fun onSpeciesSelected(id: Long) {
        _state.update { it.copy(speciesId = id) }
    }

    fun onPlaceChange(value: String) {
        _state.update { it.copy(place = value) }
    }

    fun onNoteChange(value: String) {
        _state.update { it.copy(note = value) }
    }

    fun onDateChange(millis: Long) {
        _state.update { it.copy(observedAt = millis) }
    }

    fun onPhotoChange(path: String?) {
        val previous = _state.value.photoPath
        if (previous != null && previous != path) {
            PhotoStorage.delete(previous)
        }
        _state.update { it.copy(photoPath = path) }
    }

    fun onPhotoPicked(uri: Uri) {
        viewModelScope.launch {
            val stored = withContext(Dispatchers.IO) {
                PhotoStorage.copyToStorage(getApplication(), uri)
            }
            if (stored != null) {
                _state.update { it.copy(photoPath = stored) }
            }
        }
    }

    fun save() {
        val current = _state.value
        if (!current.canSave) return
        viewModelScope.launch {
            observationRepository.save(
                ObservationEntity(
                    id = if (current.isNew) 0L else current.id,
                    speciesId = current.speciesId,
                    place = current.place.trim(),
                    observedAt = current.observedAt,
                    note = current.note.trim(),
                    photoPath = current.photoPath
                )
            )
            _state.update { it.copy(finished = true) }
        }
    }

    fun delete() {
        val current = _state.value
        if (current.isNew) return
        viewModelScope.launch {
            PhotoStorage.delete(current.photoPath)
            observationRepository.deleteById(current.id)
            _state.update { it.copy(finished = true) }
        }
    }

    override fun onCleared() {
        val current = _state.value
        if (current.isNew && !current.finished) {
            PhotoStorage.delete(current.photoPath)
        }
        super.onCleared()
    }
}
