package com.example.movieapplication.core

import com.example.movieapplication.models.CharacterResponse
import com.example.movieapplication.models.MovieResponse
import com.example.movieapplication.models.QuotesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie")
    suspend fun getMovies(@Query("page") page: Int, @Query("limit") limit: Int): MovieResponse

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int, @Query("limit") limit: Int): CharacterResponse

    @GET("character/{characterId}")
    suspend fun getCharactersDetail(@Path("characterId") characterId: String): CharacterResponse

    @GET("quote")
    suspend fun getQuote(): QuotesResponse

}