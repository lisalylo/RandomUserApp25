package com.example.randomuserapp25.data

import com.example.randomuserapp25.domain.User
import kotlinx.coroutines.flow.Flow

enum class SortField {
    NAME, BIRTHDAY
}


interface UserRepository {

    fun getUsers(): Flow<List<User>>
    fun getUsersSortedBy(field: SortField, ascending: Boolean = true): Flow<List<User>>
    suspend fun saveUser(user: User)
    suspend fun saveUsers(users: List<User>)
    suspend fun updateUser(user: User)
    suspend fun deleteAllUsers()

    suspend fun fetchRemoteUsers(count: Int = 10): List<User> //ruft user über remoteData (von website) auf - nur aufrufen, kein speichern
    /**
     * Lädt `count` User von der API, leert lokale DB und speichert die neu
     * geladenen User in der DB
     */
    suspend fun refreshUsersFromRemote(count: Int = 10)
}