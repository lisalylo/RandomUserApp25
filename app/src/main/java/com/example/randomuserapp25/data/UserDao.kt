package com.example.randomuserapp25.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.randomuserapp25.domain.User
import com.example.randomuserapp25.domain.UserEntity
import kotlinx.coroutines.flow.Flow

//Schnittstelle DB Zugriff (wie es erzeugt werden soll -> appModule)
@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: String)

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllSortedByNameAsc(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY name DESC")
    fun getAllSortedByNameDesc(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY birthDate ASC")
    fun getAllSortedByBirthdayAsc(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY birthDate DESC")
    fun getAllSortedByBirthdayDesc(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY phone ASC")
    fun getAllSortedByIdAsc(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY phone DESC")
    fun getAllSortedByIdDesc(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Update
    suspend fun update(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}

