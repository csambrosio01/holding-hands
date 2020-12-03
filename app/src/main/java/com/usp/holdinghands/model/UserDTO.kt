package com.usp.holdinghands.model

import java.util.*

data class UserDTO(
    val name: String,
    val helpTypes: List<HelpType>,
    val gender: Gender,
    val profession: String,
    val birth: String,
    val email: String,
    val phone: String,
    val password: String,
    val isHelper: Boolean,
    val latitude: Double,
    val longitude: Double
)

data class LoggedUser(
    val userId: Long,
    val name: String,
    val helpTypes: String,
    val gender: Gender,
    val profession: String,
    val email: String,
    val phone: String,
    val isHelper: Boolean,
    val birth: Date,
    val imageId: String,
    val rating: Double
)
