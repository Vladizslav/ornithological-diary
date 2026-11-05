package com.example.ornithologicaldiary.ui.screens.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ornithologicaldiary.R
import com.example.ornithologicaldiary.data.remote.dto.TaxonDto
import com.example.ornithologicaldiary.data.repository.BirdApiRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BirdSearchState {
    data object Idle : BirdSearchState
    data object Loading : BirdSearchState
    data class Success(val birds: List<TaxonDto>) : BirdSearchState
    data class Error(val message: String) : BirdSearchState
}

class BirdSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BirdApiRepository()

    private val _state = MutableStateFlow<BirdSearchState>(BirdSearchState.Idle)
    val state: StateFlow<BirdSearchState> = _state.asStateFlow()

    private var lastQuery: String = ""

    fun search(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return
        lastQuery = trimmed
        _state.value = BirdSearchState.Loading
        viewModelScope.launch {
            try {
                val birds = repository.searchBirds(trimmed)
                _state.value = BirdSearchState.Success(birds)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                _state.value = BirdSearchState.Error(
                    getApplication<Application>().getString(R.string.search_error_network)
                )
            } catch (exception: HttpException) {
                _state.value = BirdSearchState.Error(
                    getApplication<Application>().getString(
                        R.string.search_error_server,
                        exception.code()
                    )
                )
            } catch (exception: Exception) {
                _state.value = BirdSearchState.Error(
                    getApplication<Application>().getString(
                        R.string.search_error_unknown,
                        exception.message
                            ?: getApplication<Application>().getString(R.string.search_error_unknown_reason)
                    )
                )
            }
        }
    }

    fun retry() {
        if (lastQuery.isNotEmpty()) {
            search(lastQuery)
        }
    }
}
