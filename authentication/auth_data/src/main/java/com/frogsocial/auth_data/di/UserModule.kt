package com.frogsocial.auth_data.di

import android.app.Application
import androidx.room.Room
import com.frogsocial.auth_data.local.UserDao
import com.frogsocial.auth_data.local.UserDatabase
import com.frogsocial.auth_data.repository.UserRepositoryImpl
import com.frogsocial.auth_domain.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    @Singleton
    fun provideUserDatabase(app: Application): UserDao {
        return Room.databaseBuilder(
            app, UserDatabase::class.java, "user_db"
        ).build().dao
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        dao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(dao)
    }

}