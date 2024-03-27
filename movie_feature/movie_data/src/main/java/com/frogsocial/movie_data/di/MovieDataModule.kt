package com.frogsocial.movie_data.di

import com.frogsocial.movie_data.remote.MovieApi
import com.frogsocial.movie_data.repository.MovieRepositoryImpl
import com.frogsocial.movie_domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieDataModule {


    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(MovieApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApi
    ): MovieRepository {
        return MovieRepositoryImpl(api)
    }



}