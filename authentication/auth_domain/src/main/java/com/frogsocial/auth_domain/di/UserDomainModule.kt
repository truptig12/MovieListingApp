package com.frogsocial.auth_domain.di

import com.frogsocial.auth_domain.UserRepository
import com.frogsocial.auth_domain.repository.BiometricRepository
import com.frogsocial.auth_domain.use_cases.BiometricAuthenticationUseCase
import com.frogsocial.auth_domain.use_cases.UserUseCase
import com.frogsocial.auth_domain.use_cases.UserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideUsersUseCase(userRepository: UserRepository) =
        UserUseCaseImpl(userRepository) as UserUseCase

    @Provides
    @Singleton
    fun provideBiometricUseCase(biometricRepository: BiometricRepository) =
        BiometricAuthenticationUseCase(biometricRepository)
}