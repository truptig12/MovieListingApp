package com.frogsocial.movie_data.mapper

import com.frogsocial.movie_data.remote.dto.MovieDto
import com.frogsocial.movie_domain.model.Movie

/*fun MovieResponseDto.mapFromEntity() = Movie(
    id = this.id,
    adult = this.adult,
    backdropPath = this.backdropPath.orEmpty(),
    genreIds = this.genreIds,
    originalLanguage = this.originalLanguage.orEmpty(),
    originalTitle = this.originalTitle.orEmpty(),
    overview = this.overview.orEmpty(),
    popularity = this.popularity,
    posterPath = this.posterPath.orEmpty(),
    releaseDate = this.releaseDate.orEmpty(),
    title = this.title.orEmpty(),
    video = this.video,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount
)*/

fun MovieDto.mapFromEntity() = Movie(
    Poster = this.Poster,
    Title = this.Title,
    Type = this.Type,
    Year = this.Year,
    imdbID = this.imdbID
)


fun Movie.mapFromDomain() = MovieDto(
    Poster = this.Poster,
    Title = this.Title,
    Type = this.Type,
    Year = this.Year,
    imdbID = this.imdbID
)

//fun List<MovieResponseDto>.mapFromListModel(): List<Movie> {
//    return this.map {
//        it.mapFromEntity()
//    }
//}



fun List<Movie>.mapFromListDomain(): List<MovieDto> {
    return this.map {
        it.mapFromDomain()
    }
}