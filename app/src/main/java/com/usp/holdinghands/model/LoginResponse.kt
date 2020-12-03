package com.usp.holdinghands.model

data class LoginResponse(
    val user: LoggedUser,
    val token: String
)
