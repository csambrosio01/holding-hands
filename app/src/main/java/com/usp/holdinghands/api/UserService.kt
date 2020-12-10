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
    fun getUsers(
        @Header("Authorization") authorization: String,
        @Body location: Location,
        @Query("distance") distance: Double? = null,
        @Query("gender") gender: String? = null,
        @Query("ageMin") ageMin: Int? = null,
        @Query("ageMax") ageMax: Int? = null,
        @Query("helpNumberMin") helpNumberMin: Int? = null,
        @Query("helpNumberMax") helpNumberMax: Int? = null,
        @Query("helpTypes") helpTypes: String? = null
    ): Call<List<UserResponse>>

    @POST("/api/user/report")
    fun report(
        @Header("Authorization") authorization: String,
        @Body report: Report
    ): Call<ReportResponse>

    @POST("/api/user/rate")
    fun rate(@Header("Authorization") authorization: String, @Body rating: Rating): Call<Double>

    @POST("/api/user/update")
    fun update(@Header("Authorization") authorization: String): Call<UserResponse>
}
