package com.usp.holdinghands.api

import com.usp.holdinghands.model.LoginDTO
import com.usp.holdinghands.model.LoginResponse
import com.usp.holdinghands.model.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/api/user/create")
    fun createUser(@Body user: UserDTO): Call<LoginResponse>

    @POST("/api/user/login")
    fun login(@Body login: LoginDTO): Call<LoginResponse>
}
