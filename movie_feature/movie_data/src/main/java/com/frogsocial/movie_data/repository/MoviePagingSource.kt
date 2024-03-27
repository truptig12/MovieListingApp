package com.frogsocial.movie_data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.frogsocial.movie_data.remote.datasource.MovieRemoteDataSource
import com.frogsocial.movie_domain.model.Movie
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val remoteDataSource: MovieRemoteDataSource,
    private val input : String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val movies = remoteDataSource.getMovies(
                input = input,
                pageNumber = currentPage
            )
            if (!movies.Search.isNullOrEmpty()){
                LoadResult.Page(
                    data = movies.Search!!.filter { it.Year.toInt() > 2000 }.sortedBy { it.Year }.map { it.toMovie() },
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (movies.Search!!.isEmpty()) null else currentPage!! + 1
                )
            }else{
                return LoadResult.Error(Exception("No movies found for given text", Throwable()))
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

}