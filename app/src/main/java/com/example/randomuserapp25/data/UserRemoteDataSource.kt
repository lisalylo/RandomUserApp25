package com.example.randomuserapp25.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor() {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchUsers(count: Int = 10): List<UserDto> = withContext(Dispatchers.IO) {
        val url = URL("https://randomuser.me/api/?results=$count")
        (url.openConnection() as HttpURLConnection).run {
            connectTimeout = 10_000
            readTimeout    = 10_000
            requestMethod  = "GET"
            inputStream.bufferedReader().use { reader ->
                val text = reader.readText()
                // hier kommt die reified-Extension zum Einsatz:
                val dto: ApiResponseDto = json.decodeFromString(text)
                return@withContext dto.results
            }
        }
    }
}
