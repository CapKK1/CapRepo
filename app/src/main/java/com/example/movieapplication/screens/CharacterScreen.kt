package com.example.movieapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapplication.models.Characters
import com.example.movieapplication.viewModels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(onCharacterClick: (String) -> Unit, viewModel: CharactersViewModel = hiltViewModel()) {//(navController: NavController,id:String, viewModel: CharactersViewModel = hiltViewModel()) {

    val characters by viewModel.character.collectAsState()
    val isLoading by remember { viewModel.isLoading }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters List") },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Cyan,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, contentPadding = paddingValues) {
            items(characters) { character ->
                CharacterItem(character, onClick = { onCharacterClick(character._id) })
            }
            item {
                if (isLoading) {
                    CircularProgressIndicator() //align(Alignment.CenterHorizontally))
                } else if (viewModel.hasMorePages.value) {
                    LaunchedEffect(Unit) {
                        viewModel.loadCharacters()
                    }
                }
            }
        }
    }
}
@Composable
fun CharacterItem(person: Characters, onClick: () -> Unit) {
    Surface(  modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(5.dp).clickable(onClick = onClick)) {

        Box(
            modifier = Modifier.fillMaxSize(), // Fills the available space
            contentAlignment = Alignment.Center // Centers the content
        ) {
            Column(
                modifier = Modifier.height(50.dp).padding(start = 10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(text = person.name)
                Text(text = "Gender: ${person.gender} ")
                Divider()
            }
        }
    }
}

