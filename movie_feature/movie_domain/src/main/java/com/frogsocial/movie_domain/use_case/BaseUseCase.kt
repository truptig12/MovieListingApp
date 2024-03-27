package com.frogsocial.movie_domain.use_case

interface BaseUseCase<In, Out>{
    suspend fun execute(input: In): Out
}