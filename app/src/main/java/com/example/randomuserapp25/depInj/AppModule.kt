package com.example.randomuserapp25.depInj

import android.content.Context
import androidx.room.Room
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    fun provideApi(retrofit: Retrofit): RandomUserApi =
        retrofit.create(RandomUserApi::class.java)

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "users.db").build()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideUserRepository(api: RandomUserApi, dao: UserDao): UserRepository =
        UserRepositoryImpl(api, dao)
}

// RandomUserApi.kt
interface RandomUserApi {
    @GET("api/?results=10")
    suspend fun getUsers(): ApiResponseDto
}

// UserRepository.kt
interface UserRepository {
    suspend fun fetchRemoteUsers(): List<User>
    fun getLocalUsers(): Flow<List<User>>
    suspend fun saveUser(user: User)
}

// UserRepositoryImpl.kt
class UserRepositoryImpl @Inject constructor(
    private val api: RandomUserApi,
    private val dao: UserDao
) : UserRepository {
    override suspend fun fetchRemoteUsers(): List<User> {
        return api.getUsers().results.map { it.toDomain() }
    }

    override fun getLocalUsers(): Flow<List<User>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun saveUser(user: User) {
        dao.insert(user.toEntity())
    }
}