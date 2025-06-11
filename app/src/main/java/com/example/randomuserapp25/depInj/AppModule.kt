package com.example.randomuserapp25.depInj

import android.content.Context
import androidx.room.Room
import com.example.randomuserapp25.data.AppDatabase
import com.example.randomuserapp25.data.UserDao
import com.example.randomuserapp25.data.UserRemoteDataSource
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.data.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "users.db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao =
        db.userDao()

    //RemoteDataSource (im NetworkModule bereitgestellt)
    @Provides
    fun provideUserRepository(
        dao: UserDao,
        remoteDataSource: UserRemoteDataSource
    ): UserRepository =
        UserRepositoryImpl(dao, remoteDataSource)
}
