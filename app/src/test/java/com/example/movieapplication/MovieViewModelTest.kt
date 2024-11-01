package com.example.movieapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieapplication.core.MovieRepository
import com.example.movieapplication.models.Movie
import com.example.movieapplication.models.MovieResponse
import com.example.movieapplication.viewModels.MovieViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieViewModel: MovieViewModel
    private val repository: MovieRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        movieViewModel = MovieViewModel(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovies should load movies and update state`() = runTest {
        val movieResponse = MovieResponse(docs = listOf(Movie("1",1,1,1.0,2, "Movie 1",1.0,1)), pages = 2, page = 1, offset = 1, total = 1, limit = 1)
        coEvery { repository.getMovies(1, limit = 10) } returns movieResponse

        movieViewModel.loadMovies()

        movieViewModel.movies.collect { movies ->
            assertEquals(1, movies.size)
            assertEquals("Movie 1", movies[0].name)
        }
        assertEquals(false, movieViewModel.isLoading.value)
        assertTrue(movieViewModel.hasMorePages.value)
        coVerify { repository.getMovies(1, limit = 10) }
    }

    @Test
    fun `loadMovies should handle loading state correctly`() = runTest {
        movieViewModel.isLoading.value = true

        movieViewModel.loadMovies()

        coVerify(exactly = 0) { repository.getMovies(any(), limit = any()) }
    }

    @Test
    fun `loadMovies should handle pagination correctly`() = runTest {
        movieViewModel.hasMorePages.value = true
        val movieResponse = MovieResponse(docs = listOf(Movie("1",1,1,1.0,1, "Movie 1",1.0,1)), pages = 3, page = 1, limit = 1, offset = 2, total = 1)
        coEvery { repository.getMovies(1, limit = 10) } returns movieResponse

        movieViewModel.loadMovies()

        movieViewModel.movies.collect { movies ->
            assertEquals(1, movies.size)
            assertEquals("Movie 1", movies[0].name)
        }
        assertEquals(2, movieViewModel)
        assertTrue(movieViewModel.hasMorePages.value)

        coEvery { repository.getMovies(2, limit = 10) } returns MovieResponse(docs = listOf(Movie("1",1,1,1.0,2, "Movie 1",1.0,1)), pages = 2, page = 1, offset = 1, total = 1, limit = 1)
        movieViewModel.loadMovies()

        movieViewModel.movies.collect { movies ->
            assertEquals(2, movies.size)
            assertEquals("Movie 2", movies[1].name)
        }
    }

    @Test
    fun `loadMovies should handle exceptions gracefully`() = runTest {
        coEvery { repository.getMovies(any(), limit = any()) } throws Exception("Network Error")

        movieViewModel.loadMovies()

        assertEquals(false, movieViewModel.isLoading.value)
    }
}
