package com.example.randomuserapp25.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.randomuserapp25.domain.UserEntity

/**
 * Haupt‐DB-KLasse für Room
 * Room generiert automatisch Implementierung
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}