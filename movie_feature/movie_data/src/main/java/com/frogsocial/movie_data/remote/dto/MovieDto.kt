package com.frogsocial.movie_data.remote.dto


import com.frogsocial.movie_domain.model.Movie

data class MovieDto(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
) {
  /*  fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            Poster = Poster,
            Title = Title,
            Type = Type,
            Year = Year,
            imdbID = imdbID
        )
    }*/

    fun toMovie() : Movie{
        return Movie(
            Poster,
          Title,
           Type,
         Year,
          imdbID
        )
    }

    companion object{
        fun fromMovie(movie : Movie) : MovieDto{
            return MovieDto(
                movie.Poster,
                movie.Title,
                movie.Type,
                movie.Year,
                movie.imdbID
            )
        }
    }
}