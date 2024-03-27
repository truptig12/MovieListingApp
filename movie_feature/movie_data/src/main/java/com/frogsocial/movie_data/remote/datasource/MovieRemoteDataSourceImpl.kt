package com.frogsocial.movie_data.remote.datasource


import com.frogsocial.core.utils.MoviesConstants
import com.frogsocial.movie_data.remote.MovieApi
import com.frogsocial.movie_data.remote.dto.MovieDto
import com.frogsocial.movie_data.remote.dto.SearchResultsDto
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val api: MovieApi,
) : MovieRemoteDataSource {

    override suspend fun getMovies(
        input: String,
        pageNumber: Int
    ): SearchResultsDto<List<MovieDto>> {
        val searchString = if(input.isEmpty()) "Love" else input
        return api.getAllMovies(searchString, apiKey = MoviesConstants.MOVIE_API_KEY, page = pageNumber)
    }

}