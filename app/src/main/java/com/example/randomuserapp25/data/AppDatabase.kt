package com.example.randomuserapp25.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.randomuserapp25.domain.UserEntity

/**
 * Haupt‐Datenbankklasse für Room
 * Room generiert automatisch die Implementierung AppDatabase_Impl
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    /** Liefert das DAO für User‐Operationen */
    abstract fun userDao(): UserDao
}