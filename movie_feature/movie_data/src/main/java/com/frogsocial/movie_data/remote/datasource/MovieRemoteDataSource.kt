package com.frogsocial.movie_data.remote.datasource

import com.frogsocial.movie_data.remote.dto.MovieDto
import com.frogsocial.movie_data.remote.dto.SearchResultsDto


interface MovieRemoteDataSource {

    suspend fun getMovies(
        input : String,
        pageNumber: Int
    ): SearchResultsDto<List<MovieDto>>

}
