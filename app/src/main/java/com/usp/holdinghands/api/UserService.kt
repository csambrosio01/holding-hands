package com.usp.holdinghands.api

import com.usp.holdinghands.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST("/api/user/create")
    fun createUser(@Body user: UserDTO): Call<LoginResponse>

    @POST("/api/user/login")
    fun login(@Body login: LoginDTO): Call<LoginResponse>

    @POST("/api/user")
    fun getUsers(@Header("Authorization") authorization: String, @Body location: Location, @Query("distance") distance: Double? = null): Call<List<UserResponse>>

    @POST("/api/user/report")
    fun report(@Header("Authorization") authorization: String, @Body report: Report): Call<ReportResponse>
}
