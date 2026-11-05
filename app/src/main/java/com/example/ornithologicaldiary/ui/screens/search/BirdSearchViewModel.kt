package com.example.ornithologicaldiary.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class BirdSearchViewModel : ViewModel() {

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
                    "Нет соединения с интернетом. Проверьте сеть и повторите попытку."
                )
            } catch (exception: HttpException) {
                _state.value = BirdSearchState.Error(
                    "Сервер вернул ошибку ${exception.code()}. Попробуйте позже."
                )
            } catch (exception: Exception) {
                _state.value = BirdSearchState.Error(
                    "Не удалось загрузить данные: ${exception.message ?: "неизвестная ошибка"}"
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
