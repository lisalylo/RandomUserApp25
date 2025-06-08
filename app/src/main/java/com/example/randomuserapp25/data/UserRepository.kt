package com.example.randomuserapp25.data

import com.example.randomuserapp25.domain.User
import kotlinx.coroutines.flow.Flow

enum class SortField {
    NAME, BIRTHDAY, PHONENUMBER
}

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    fun getUsersSortedBy(field: SortField, ascending: Boolean = true): Flow<List<User>>
    suspend fun saveUser(user: User)
    suspend fun saveUsers(users: List<User>)
    suspend fun updateUser(user: User)
    suspend fun deleteAllUsers()
}