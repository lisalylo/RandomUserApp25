package com.example.randomuserapp25.depInj

import com.example.randomuserapp25.data.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * API-Zugriffe global über Hilt verfügbar machen
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    /**
     * Instanz Remote-Datenquelle zu verfügung stellen
     */
    @Provides
    @Singleton
    fun provideUserRemoteDataSource(): UserRemoteDataSource =
        UserRemoteDataSource()
}
