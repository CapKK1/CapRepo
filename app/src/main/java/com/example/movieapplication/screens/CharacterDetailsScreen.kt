package com.example.movieapplication.screens

import android.widget.Switch
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapplication.models.Quotes
import com.example.movieapplication.viewModels.CharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(characterId: String?, viewModel: CharacterDetailViewModel = hiltViewModel()) {
    val characterDetails by viewModel.characterDetails.observeAsState()
    val quotes by viewModel.quotes.collectAsState()
    val isLoading by remember { viewModel.isLoading }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Detail") },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Cyan,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        characterDetails?.let { details ->
            if (isLoading) {
                Box(Modifier.fillMaxSize()){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).padding(top = 100.dp)) //align(Alignment.CenterHorizontally))
                }
            } else{
            Column(modifier = Modifier.fillMaxWidth().padding(paddingValues)) {
                val isEnabled = FeatureFlags.enableQuotes

                Row(modifier = Modifier.align(Alignment.End).padding(end = 5.dp)) {
                    Text("Enable Quotes", modifier = Modifier.align(Alignment.CenterVertically).padding(end = 2.dp))
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = {
                            FeatureFlags.enableQuotes = it

                        }
                    )
                }

                Text(
                    " ${details.docs[0].name}",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp),
                    color = Color.Blue,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Gender: ${details.docs[0].gender}",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 2.dp)
                )
                Text(
                    "Birth: ${details.docs[0].birth ?: "Not Available"}",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 2.dp)
                )
                Text(
                    "Height: ${details.docs[0].height ?: "Not Available"}",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 2.dp)
                )
                Text(
                    "Spouse: ${details.docs[0].spouse}",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 2.dp)
                )
                Text(
                    "Wiki URL: ${details.docs[0].wikiUrl}",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 2.dp)
                )
                if (FeatureFlags.enableQuotes) {
                    LazyRow(verticalAlignment = Alignment.CenterVertically, contentPadding = paddingValues, modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                        items(quotes) { quote ->
                            DisplayQuotes(quote)
                        }
                    }

                }
            }
        }}
    }
    LaunchedEffect(characterId) {
        viewModel.loadQuotes()
        if (characterId != null) {
            viewModel.loadCharacterDetails(characterId)
        }
    }
}
@Composable
fun DisplayQuotes(quotes: Quotes){
    Row (){
        Surface( color = Color.Yellow, modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(5.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth(), // Fills the available space
                contentAlignment = Alignment.Center // Centers the content
            ) {
                Text(text = quotes.dialog,Modifier.height(40.dp).padding(10.dp), textAlign = TextAlign.Center)

            }

        }
    }

}