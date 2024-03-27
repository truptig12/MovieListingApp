package com.frogsocial.movie_presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.frogsocial.movie_domain.model.Movie
import com.frogsocial.movie_domain.use_case.GetMovieData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var getMovieData: GetMovieData

    private lateinit var movieViewModel: MovieViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        kotlinx.coroutines.Dispatchers.setMain(testDispatcher)

        movieViewModel = MovieViewModel(getMovieData)
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getHomeEvenTest() = runTest{
        val mockPagingData = PagingData.from(listOf(Movie("poster", "title", "Action", "2000","imdb")))
        val flow: Flow<PagingData<Movie>> = flowOf(mockPagingData)

        `when`(getMovieData.execute("")).thenReturn(flow)
        movieViewModel.moviesState.test {
            movieViewModel.onEvent(HomeEvent.GetHome)
            val emission1 = awaitItem()
            assertEquals(emission1::class, mockPagingData::class)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchEventTest() = runTest{
        val searchString = "Matrix"
        val mockPagingData = PagingData.from(listOf(Movie("poster", "title", "Action", "2000","imdb")))
        val flow: Flow<PagingData<Movie>> = flowOf(mockPagingData)

        `when`(getMovieData.execute(searchString)).thenReturn(flow)

        movieViewModel.moviesState.test {
            movieViewModel.onEvent(HomeEvent.Search(searchString))
            val emission1 = awaitItem()
            assertEquals(emission1::class, mockPagingData::class)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
