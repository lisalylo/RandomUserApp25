package com.example.randomuserapp25.depInj

import android.content.Context
import androidx.room.Room
import com.example.randomuserapp25.data.AppDatabase
import com.example.randomuserapp25.data.UserDao
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.data.UserRepositoryImpl
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "users.db").build()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideUserRepository(dao: UserDao): UserRepository = UserRepositoryImpl(dao)
}