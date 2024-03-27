package com.frogsocial.movie_presentation

import com.frogsocial.movie_domain.model.Movie


data class MovieDataState(
    val movieItems: List<Movie> = emptyList(),
    val isLoading: Boolean = false
)