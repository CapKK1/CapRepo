package com.example.movieapplication.models

data class Movie(
    val _id: String,
    val academyAwardNominations: Int,
    val academyAwardWins: Int,
    val boxOfficeRevenueInMillions: Double,
    val budgetInMillions: Int,
    val name: String,
    val rottenTomatoesScore: Double,
    val runtimeInMinutes: Int
)