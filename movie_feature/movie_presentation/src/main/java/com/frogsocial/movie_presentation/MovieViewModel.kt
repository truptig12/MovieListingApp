package com.frogsocial.movie_presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.frogsocial.core.utils.MoviesConstants.Companion.DEFAULT
import com.frogsocial.movie_domain.model.Movie
import com.frogsocial.movie_domain.use_case.GetMovieData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMovieData: GetMovieData
) : ViewModel() {

    private val _moviesState: MutableStateFlow<PagingData<Movie>> =
        MutableStateFlow(value = PagingData.empty())
    val moviesState: MutableStateFlow<PagingData<Movie>> get() = _moviesState
    var userAuthState = mutableStateOf(true)

    init {
        onEvent(HomeEvent.GetHome)
    }

    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.GetHome -> {
                    getMovies(DEFAULT)
                }

                is HomeEvent.Search -> {
                    getMovies(event.input)
                }
                is HomeEvent.Login -> {
                    userAuthState.value = true
                }

                else -> {}
            }
        }
    }


    private suspend fun getMovies(input: String) {
        getMovieData.execute(input)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _moviesState.value = it
            }
    }
}

sealed class HomeEvent {
    object GetHome : HomeEvent()
    class Search(val input: String) : HomeEvent()
    class Login() : HomeEvent()

}

data class HomeState(
    val movies: List<Movie> = listOf()
)