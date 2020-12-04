package com.usp.holdinghands.model

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
