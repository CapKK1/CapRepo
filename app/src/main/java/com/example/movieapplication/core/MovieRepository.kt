package com.example.movieapplication.core

import com.example.movieapplication.models.CharacterResponse
import com.example.movieapplication.models.MovieResponse
import com.example.movieapplication.models.QuotesResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService
) {

    suspend fun getMovies(page: Int, limit: Int): MovieResponse {
        return apiService.getMovies(page, limit)
    }

    suspend fun getQuotes(): QuotesResponse {
        return apiService.getQuote()
    }

    suspend fun getCharacters(page: Int, limit: Int): CharacterResponse {
        return apiService.getCharacters(page, limit)
    }

    suspend fun getCharactersDetail(characterId: String): CharacterResponse = apiService.getCharactersDetail(characterId)

}