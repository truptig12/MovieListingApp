package com.frogsocial.movie_domain.model

data class Movie(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    var imdbID: String
)
