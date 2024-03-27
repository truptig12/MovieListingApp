package com.frogsocial.movie_data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.frogsocial.movie_data.remote.MovieApi
import com.frogsocial.movie_data.remote.datasource.MovieRemoteDataSourceImpl
import com.frogsocial.movie_domain.model.Movie
import com.frogsocial.movie_domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val api: MovieApi) : MovieRepository {

    override fun getMovie(input : String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                MoviePagingSource(MovieRemoteDataSourceImpl(api), input)
            }
        ).flow
    }
}