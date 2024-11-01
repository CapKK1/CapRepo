package com.example.movieapplication.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.core.MovieRepository
import com.example.movieapplication.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private var currentPage = 1
    var isLoading = mutableStateOf(false)
    var hasMorePages = mutableStateOf(true)

    init {
        loadMovies()
    }

    fun loadMovies() {
        if (!isLoading.value && hasMorePages.value) {
            viewModelScope.launch {
                isLoading.value = true
                try {
                    val response = repository.getMovies(currentPage, limit = 10)
                    _movies.value = _movies.value + response.docs
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
