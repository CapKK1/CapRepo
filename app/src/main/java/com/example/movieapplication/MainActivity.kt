package com.example.movieapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapplication.screens.CharacterDetailsScreen
import com.example.movieapplication.screens.CharacterScreen
import com.example.movieapplication.screens.MovieListScreen
import com.example.movieapplication.ui.theme.MovieApplicationTheme
import com.example.movieapplication.viewModels.CharactersViewModel
import com.example.movieapplication.viewModels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun MovieApp() {

    val navController = rememberNavController()
    val movieViewModel: MovieViewModel = hiltViewModel()
    val characterViewModel: CharactersViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "movies") {
        composable("movies") {
            MovieListScreen(
                onMovieSelected = { movieId ->
                    navController.navigate("characterScreen/$movieId")
                },
                viewModel = movieViewModel
            )
        }
        composable("characterScreen/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            if (movieId != null) {
                CharacterScreen(
                    onCharacterClick = { characterId ->
                        navController.navigate("characterDetails/$characterId")
                    },
                    viewModel = characterViewModel // Pass the character view model
                )
            }
        }
        composable("characterDetails/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            CharacterDetailsScreen(characterId) // Ensure this screen can handle a null characterId
        }
    }
}