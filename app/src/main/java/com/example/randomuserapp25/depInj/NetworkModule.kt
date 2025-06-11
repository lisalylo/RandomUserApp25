/*package com.example.randomuserapp25.depInj

import com.example.randomuserapp25.data.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton
//import io.ktor.client.engine.cio.CIO
//import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.android.Android

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideKtorClient(): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level  = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 20_000
            socketTimeoutMillis  = 20_000
        }
    }

    @Provides @Singleton
    fun provideUserRemoteDataSource(client: HttpClient) =
        UserRemoteDataSource(client)
}*/