package com.example.movieapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieapplication.core.MovieRepository
import com.example.movieapplication.models.CharacterResponse
import com.example.movieapplication.models.Characters
import com.example.movieapplication.viewModels.CharactersViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharactersViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var charactersViewModel: CharactersViewModel
    private val repository: MovieRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        charactersViewModel = CharactersViewModel(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCharacters should load characters and update state`() = runTest {
        val characterResponse = CharacterResponse(docs = listOf(Characters("1", "Character 1","","","","","","","","","")),1,1,1,1,1)
        coEvery { repository.getCharacters(1, limit = 10) } returns characterResponse

        charactersViewModel.loadCharacters()

        charactersViewModel.character.collect { characters ->
            assertEquals(1, characters.size)
            assertEquals("Character 1", characters[0].name)
        }
        assertEquals(true, charactersViewModel.hasMorePages.value)
    }

    @Test
    fun `loadCharacters should not load if already loading`() = runTest {
        charactersViewModel.isLoading.value = true

        charactersViewModel.loadCharacters()

        coVerify(exactly = 0) { repository.getCharacters(any(), limit = any()) }
    }

    @Test
    fun `loadCharacters should not load if there are no more pages`() = runTest {
        charactersViewModel.hasMorePages.value = false

        charactersViewModel.loadCharacters()

        coVerify(exactly = 0) { repository.getCharacters(any(), limit = any()) }
    }

    @Test
    fun `loadCharacters should handle exceptions gracefully`() = runTest {
        coEvery { repository.getCharacters(1, limit = 10) } throws Exception("Network Error")

        charactersViewModel.loadCharacters()

        assertEquals(false, charactersViewModel.isLoading.value)
    }
}
