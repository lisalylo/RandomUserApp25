package com.example.randomuserapp25.data

import com.example.randomuserapp25.domain.User

//Struktur Response API Response (JSON Daten in kotlin objekte deserializen)
//nur Datentransport -> deshalb Dto
data class ApiResponseDto(
    val results: List<UserDto>
)

data class UserDto(
    val login: Login,
    val name: Name,
    val dob: Dob,
    val phone: String,
    val picture: Picture
)

data class Login(val uuid: String)
data class Name(val first: String, val last: String)
data class Dob(val date: String)
data class Picture(val large: String)

fun UserDto.toDomain(): User = User(
    id = login.uuid,
    name = "${name.first} ${name.last}",
    photoUrl = picture.large,
    birthDate = dob.date,
    phone = phone
)