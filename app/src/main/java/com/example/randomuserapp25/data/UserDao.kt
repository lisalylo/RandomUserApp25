package com.example.randomuserapp25.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.randomuserapp25.domain.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO für alle Datenbank‐Operationen rund um die Tabelle "users".
 * Arbeitet ausschließlich mit UserEntity, damit Room KSP korrekt verarbeitet.
 */
@Dao
interface UserDao {

    /** Liefert alle User als Flow von Entity-Objekten */
    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    /** Liefert alle User ohne Flow, als einfache Liste */
    @Query("SELECT * FROM users")
    suspend fun getAllOnce(): List<UserEntity>

    /** Sortiert nach Name aufsteigend */
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllSortedByNameAsc(): Flow<List<UserEntity>>

    /** Sortiert nach Name absteigend */
    @Query("SELECT * FROM users ORDER BY name DESC")
    fun getAllSortedByNameDesc(): Flow<List<UserEntity>>

    /** Sortiert nach Geburtstag aufsteigend */
    @Query("SELECT * FROM users ORDER BY birthDate ASC")
    fun getAllSortedByBirthdayAsc(): Flow<List<UserEntity>>

    /** Sortiert nach Geburtstag absteigend */
    @Query("SELECT * FROM users ORDER BY birthDate DESC")
    fun getAllSortedByBirthdayDesc(): Flow<List<UserEntity>>

    /** Sortiert nach Telefonnummer aufsteigend */
    @Query("SELECT * FROM users ORDER BY phone ASC")
    fun getAllSortedByPhoneAsc(): Flow<List<UserEntity>>

    /** Sortiert nach Telefonnummer absteigend */
    @Query("SELECT * FROM users ORDER BY phone DESC")
    fun getAllSortedByPhoneDesc(): Flow<List<UserEntity>>

    /** Fügt einen UserEntity ein (oder ersetzt bei Konflikt) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    /** Fügt mehrere UserEntitys ein (oder ersetzt bei Konflikt) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    /** Aktualisiert einen bestehenden UserEntity */
    @Update
    suspend fun update(user: UserEntity)

    /** Löscht einen einzelnen User anhand der ID */
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteById(userId: String)

    /** Löscht alle User aus der Tabelle */
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}


