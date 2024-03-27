package com.frogsocial.movie_domain.repository

import androidx.paging.PagingData
import com.frogsocial.core.utils.Resource
import com.frogsocial.movie_domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovie(input : String): Flow<PagingData<Movie>>

}