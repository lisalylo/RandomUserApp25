package com.example.randomuserapp25.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

//datenbankbezogen
/*@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val photoUrl: String,
    val birthDate: String,
    val phone: String
)

fun UserEntity.toDomain() = User(id, name, photoUrl, birthDate, phone)
fun User.toEntity() = UserEntity(id, name, photoUrl, birthDate, phone)*/


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val birthDate: String,
    val phone: String,
    val photoUrl: String
)

data class User(
    val id: String,
    val name: String,
    val photoUrl: String,
    val birthDate: String,
    val phone: String
)

/** Extension von Entity → Domain */
fun UserEntity.toDomain(): User = User(
    id        = id,
    name      = name,
    photoUrl  = photoUrl,
    birthDate = birthDate,
    phone     = phone
)

/** Extension von Domain → Entity */
fun User.toEntity(): UserEntity = UserEntity(
    id        = id,
    name      = name,
    birthDate = birthDate,
    phone     = phone,
    photoUrl  = photoUrl
)
