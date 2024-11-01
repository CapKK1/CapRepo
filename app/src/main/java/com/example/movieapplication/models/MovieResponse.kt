package com.example.movieapplication.models

data class MovieResponse(
    val docs: List<Movie>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)