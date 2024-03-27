package com.frogsocial.movie_data.remote


import com.frogsocial.movie_data.remote.dto.MovieDto
import com.frogsocial.movie_data.remote.dto.SearchResultsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/")
    suspend fun getAllMovies(
        @Query("s") searchTerm: String,
        @Query("type") type: String = "movie",
        @Query("r") r: String = "json",
        @Query("apikey") apiKey: String,
        @Query("page") page :Int
    ): SearchResultsDto<List<MovieDto>>

    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"
    }
}