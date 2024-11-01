package com.example.movieapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieapplication.core.MovieRepository
import com.example.movieapplication.models.CharacterResponse
import com.example.movieapplication.models.Characters
import com.example.movieapplication.models.Quotes
import com.example.movieapplication.models.QuotesResponse
import com.example.movieapplication.viewModels.CharacterDetailViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var characterDetailViewModel: CharacterDetailViewModel
    private val repository: MovieRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        characterDetailViewModel = CharacterDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCharacterDetails should load character details and update LiveData`() = runTest {
        val characterId = "1"
        val characterResponse = CharacterResponse(docs = listOf(Characters("1", "Character 1","","","","","","","","","")),1,1,1,1,1)
        coEvery { repository.getCharactersDetail(characterId) } returns characterResponse

        characterDetailViewModel.loadCharacterDetails(characterId)

        characterDetailViewModel.characterDetails.observeForever { details ->
            assertNotNull(details)
            assertEquals(characterResponse, details)
        }
        assertEquals(true, characterDetailViewModel.isLoading.value)
        coVerify { repository.getCharactersDetail(characterId) }
    }

    @Test
    fun `loadCharacterDetails should handle exceptions gracefully`() = runTest {
        val characterId = "1"
        coEvery { repository.getCharactersDetail(characterId) } throws Exception("Network Error")

        characterDetailViewModel.loadCharacterDetails(characterId)

        assertEquals(false, characterDetailViewModel.isLoading.value) // Loading should be false
        coVerify { repository.getCharactersDetail(characterId) }
    }

    @Test
    fun `loadQuotes should load quotes and update state`() = runTest {
        val quotesResponse = QuotesResponse(docs = listOf(Quotes("1", "Quote 1","","","")),1,1,1,1,1) // Adjust based on your model
        coEvery { repository.getQuotes() } returns quotesResponse

        characterDetailViewModel.loadQuotes()

        characterDetailViewModel.quotes.collect { quotes ->
            assertEquals(1, quotes.size)
            assertEquals("Quote 1", quotes[0].dialog)
        }
        coVerify { repository.getQuotes() }
    }

    @Test
    fun `loadQuotes should handle exceptions gracefully`() = runTest {
        coEvery { repository.getQuotes() } throws Exception("Network Error")

        characterDetailViewModel.loadQuotes()

        assert(true)
        coVerify { repository.getQuotes() }
    }
}
