package com.example.randomuserapp25.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

//datenbankbezogen
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val photoUrl: String,
    val birthDate: String,
    val phone: String
)

fun UserEntity.toDomain() = User(id, name, photoUrl, birthDate, phone)
fun User.toEntity() = UserEntity(id, name, photoUrl, birthDate, phone)
