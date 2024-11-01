package com.example.movieapplication.models

data class QuotesResponse(
    val docs: List<Quotes>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)