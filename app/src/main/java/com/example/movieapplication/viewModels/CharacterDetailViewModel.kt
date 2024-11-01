package com.example.movieapplication.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.core.MovieRepository
import com.example.movieapplication.models.CharacterResponse
import com.example.movieapplication.models.Movie
import com.example.movieapplication.models.Quotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _characterDetails = MutableLiveData<CharacterResponse>()
    val characterDetails: LiveData<CharacterResponse> = _characterDetails
    var isLoading = mutableStateOf(false)
    private val _quotes = MutableStateFlow<List<Quotes>>(emptyList())
    val quotes: StateFlow<List<Quotes>> = _quotes.asStateFlow()

    fun loadCharacterDetails(characterId: String) {
        if (!isLoading.value){
        viewModelScope.launch {
            isLoading.value = true
            try {
                _characterDetails.value = repository.getCharactersDetail(characterId)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
        }
    }
    fun loadQuotes() {
            viewModelScope.launch {
                try {
                    val response = repository.getQuotes()
                    _quotes.value = _quotes.value + response.docs

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                }
            }

    }
}