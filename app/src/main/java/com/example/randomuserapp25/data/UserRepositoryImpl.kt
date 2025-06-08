package com.example.randomuserapp25.data

import com.example.randomuserapp25.domain.User
import com.example.randomuserapp25.domain.toDomain
import com.example.randomuserapp25.domain.toEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserRepository {

    override fun getUsers(): Flow<List<User>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getUsersSortedBy(field: SortField, ascending: Boolean): Flow<List<User>> {
        val flow = when (field) {
            SortField.NAME -> if (ascending) dao.getAllSortedByNameAsc() else dao.getAllSortedByNameDesc()
            SortField.BIRTHDAY -> if (ascending) dao.getAllSortedByBirthdayAsc() else dao.getAllSortedByBirthdayDesc()
            SortField.PHONENUMBER -> if (ascending) dao.getAllSortedByIdAsc() else dao.getAllSortedByIdDesc()
        }
        return flow.map { list -> list.map { it.toDomain() } }
    }

    override suspend fun saveUser(user: User) {
        dao.insert(user.toEntity())
    }

    override suspend fun saveUsers(users: List<User>) {
        dao.insertAll(users.map { it.toEntity() })
    }

    override suspend fun updateUser(user: User) {
        dao.update(user.toEntity())
    }

    override suspend fun deleteAllUsers() {
        dao.deleteAll()
    }
}
