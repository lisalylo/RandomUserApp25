package com.example.randomuserapp25.data

import android.util.Log
import com.example.randomuserapp25.domain.User
import com.example.randomuserapp25.domain.UserEntity
import com.example.randomuserapp25.domain.toDomain
import com.example.randomuserapp25.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Implementierung Interface
 * dao Zugriff auf lokale DB
 * remote Zugriff auf Remote api (UserRemoteDataSource)
 */

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val remote: UserRemoteDataSource
) : UserRepository {

    override fun getUsers(): Flow<List<User>> =
        dao.getAll().map { list -> list.map(UserEntity::toDomain) }

    override fun getUsersSortedBy(field: SortField, ascending: Boolean): Flow<List<User>> {
        val flow = when (field) {
            SortField.NAME -> if (ascending) dao.getAllSortedByNameAsc() else dao.getAllSortedByNameDesc()
            SortField.BIRTHDAY -> if (ascending) dao.getAllSortedByBirthdayAsc() else dao.getAllSortedByBirthdayDesc()
        }
        return flow.map { list -> list.map { it.toDomain() } }
    }

    override suspend fun fetchRemoteUsers(count: Int): List<User> =
        remote.fetchUsers(count).map { dto -> dto.toDomain() }

    override suspend fun saveUser(user: User) =
        dao.insert(user.toEntity())

    override suspend fun saveUsers(users: List<User>) =
        dao.insertAll(users.map { it.toEntity() })

    override suspend fun updateUser(user: User) =
        dao.update(user.toEntity())

    override suspend fun deleteAllUsers() =
        dao.deleteAll()

    override suspend fun refreshUsersFromRemote(count: Int) {
        Log.d("Repo", "Start refreshing")
        val users = remote.fetchUsers(count).map { it.toDomain() }
        if (users.isEmpty()) {
            Log.w("Repo", "no data, aborting")
            return
        }
        dao.deleteAll()
        dao.insertAll(users.map { it.toEntity() })
        Log.d("Repo", "Inserted ${users.size} users")
    }
}
