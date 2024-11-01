package com.example.movieapplication.models

data class CharacterResponse(
    val docs: List<Characters>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)