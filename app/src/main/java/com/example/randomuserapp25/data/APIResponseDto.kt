package com.example.randomuserapp25.data

import kotlinx.serialization.Serializable
import com.example.randomuserapp25.domain.User

/**
 * Datenklasse für API-Antwort
 * API liefert JSON-Objekt
 */
@Serializable
data class ApiResponseDto(val results: List<UserDto>)

@Serializable
data class UserDto(
    val login: Login,
    val name: Name,
    val dob: Dob,
    val phone: String,
    val picture: Picture
)

@Serializable
data class Login(val uuid: String)
@Serializable
data class Name(val first: String, val last: String)
@Serializable
data class Dob(val date: String)
@Serializable
data class Picture(val large: String)

/**
 * Konvertiert UserDto in Domain-Modell User
 */

fun UserDto.toDomain(): User = User(
    id        = login.uuid,
    name      = "${name.first} ${name.last}",
    photoUrl  = picture.large,
    birthDate = dob.date,
    phone     = phone
)
