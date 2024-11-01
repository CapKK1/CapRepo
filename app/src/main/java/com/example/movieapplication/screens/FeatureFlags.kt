package com.example.movieapplication.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object FeatureFlags {
    var enableQuotes: Boolean by mutableStateOf(false)
}