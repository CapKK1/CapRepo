package com.example.movieapplication.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.core.MovieRepository
import com.example.movieapplication.models.Characters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _characters = MutableStateFlow<List<Characters>>(emptyList())
    val character: StateFlow<List<Characters>> = _characters.asStateFlow()

    private var currentPage = 1
    var isLoading = mutableStateOf(false)
    var hasMorePages = mutableStateOf(true)

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        if (!isLoading.value && hasMorePages.value) {
            viewModelScope.launch {
                isLoading.value = true
                try {

                    val response = repository.getCharacters(currentPage, limit = 10)
                    _characters.value = _characters.value + response.docs
                    currentPage++
                    hasMorePages.value = currentPage <= response.pages
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading.value = false
                }
            }
        }
    }
}