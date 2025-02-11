package com.example.precisepal.di

import com.example.precisepal.data.repository.AuthRepositoryImpl
import com.example.precisepal.data.repository.DatabaseRepositoryImpl
import com.example.precisepal.domain.repository.AuthRepository
import com.example.precisepal.domain.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    //we use provide as prefix to the function
    fun provideAuthRepository() : AuthRepository{
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    //we use provide as prefix to the function
    fun provideDataBaseRepository() : DatabaseRepository{
        return DatabaseRepositoryImpl()
    }

}