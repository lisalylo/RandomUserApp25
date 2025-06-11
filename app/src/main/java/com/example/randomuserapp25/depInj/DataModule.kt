package com.example.randomuserapp25.depInj

import com.example.randomuserapp25.data.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideUserRemoteDataSource(): UserRemoteDataSource =
        UserRemoteDataSource()
}
