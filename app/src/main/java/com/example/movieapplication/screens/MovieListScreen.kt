package com.example.movieapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapplication.models.Movie
import com.example.movieapplication.viewModels.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(onMovieSelected: (String) -> Unit, viewModel: MovieViewModel = hiltViewModel()) {

    val movies by viewModel.movies.collectAsState()
    val isLoading by remember { viewModel.isLoading }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie List") },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Cyan,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->

    LazyColumn (horizontalAlignment = Alignment.CenterHorizontally, contentPadding = paddingValues){
        items(movies) { movie ->
            MovieItem(movie, onClick = { onMovieSelected(movie._id) })
        }
        item {
            if (isLoading) {
                Box(Modifier.fillMaxSize()){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).padding(top = 100.dp)) //align(Alignment.CenterHorizontally))
                }
            } else if (viewModel.hasMorePages.value) {
                LaunchedEffect(Unit) {
                    viewModel.loadMovies()
                }
            }
        }
    }
}}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Column (){
        Surface( color = Color.Gray, modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(5.dp).clickable(onClick = onClick)) {
        Box(
            modifier = Modifier.fillMaxWidth(), // Fills the available space
            contentAlignment = Alignment.Center // Centers the content
        ) {
            Text(text = movie.name,Modifier.height(40.dp).padding(10.dp), textAlign = TextAlign.Center)

        }

    }}
}