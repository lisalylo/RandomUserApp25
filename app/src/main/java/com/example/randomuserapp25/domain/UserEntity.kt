package com.example.randomuserapp25.domain

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val birthDate: String,
    val phone: String,
    val photoUrl: String
)

/** Entity <-> Domain */
fun UserEntity.toDomain(): User = User(
    id        = id,
    name      = name,
    photoUrl  = photoUrl,
    birthDate = birthDate,
    phone     = phone
)

fun User.toEntity(): UserEntity = UserEntity(
    id        = id,
    name      = name,
    birthDate = birthDate,
    phone     = phone,
    photoUrl  = photoUrl
)
