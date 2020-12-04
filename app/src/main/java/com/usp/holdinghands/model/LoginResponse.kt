package com.usp.holdinghands.model

data class LoginResponse(
    val user: UserResponse,
    val token: String
)
