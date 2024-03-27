package com.frogsocial.movie_domain.use_case

import androidx.paging.PagingData
import com.frogsocial.core.utils.Resource
import com.frogsocial.movie_domain.model.Movie
import com.frogsocial.movie_domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetMovieData @Inject constructor(
    private val repository: MovieRepository
) : BaseUseCase<String, Flow<PagingData<Movie>>> {
    override suspend fun execute(input: String): Flow<PagingData<Movie>> {
        return repository.getMovie(input)
    }
}