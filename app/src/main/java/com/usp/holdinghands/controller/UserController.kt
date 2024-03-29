package com.usp.holdinghands.controller

import android.content.Context
import android.location.Location
import com.usp.holdinghands.api.UserService
import com.usp.holdinghands.configurations.RetrofitBuilder
import com.usp.holdinghands.model.*
import com.usp.holdinghands.utils.EnumConverter
import com.usp.holdinghands.utils.JsonUtil
import retrofit2.Callback

const val tokenKey = "token"

class UserController(val context: Context) {

    val sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE)
    val userKey = "userInfo"

    val request = RetrofitBuilder.buildService(UserService::class.java)

    fun getUsers(location: Location, filter: UserFilter?, listener: Callback<List<UserResponse>>) {
        val token = sharedPreferences.getString(tokenKey, "")!!
        val call = request.getUsers(
            token,
            Location(location.latitude, location.longitude),
            filter?.distance,
            filter?.gender?.name,
            filter?.ageMin,
            filter?.ageMax,
            filter?.helpNumberMin,
            filter?.helpNumberMax,
            EnumConverter.enumListToString(filter?.helpTypes ?: mutableListOf())
        )
        call.enqueue(listener)
    }

    fun createUser(user: UserDTO, listener: Callback<LoginResponse>) {
        val call = request.createUser(user)
        call.enqueue(listener)
    }

    fun login(loginDTO: LoginDTO, listener: Callback<LoginResponse>) {
        val call = request.login(loginDTO)
        call.enqueue(listener)
    }

    fun logout() {
        sharedPreferences
            .edit()
            .remove(userKey)
            .remove(tokenKey)
            .apply()
    }

    fun setLogin(setLogin: LoginResponse) {
        val jsonUser = JsonUtil.toJson(setLogin.user)
        sharedPreferences
            .edit()
            .putString(userKey, jsonUser)
            .putString(tokenKey, setLogin.token)
            .apply()
    }

    fun setLoggedUser(user: UserResponse) {
        val jsonUser = JsonUtil.toJson(user)
        sharedPreferences
            .edit()
            .putString(userKey, jsonUser)
            .apply()
    }

    fun getLoggedUser(): UserResponse? {
        val userString = sharedPreferences.getString(userKey, null)
        return if (userString != null) {
            JsonUtil.fromJson(userString)
        } else {
            null
        }
    }

    fun report(user: UserResponse, message: String, listener: Callback<ReportResponse>) {
        val token = sharedPreferences.getString(tokenKey, "")!!

        val report = Report(
            user.userId,
            message
        )

        val call = request.report(token, report)
        call.enqueue(listener)
    }

    fun rate(user: UserResponse, rate: Float, listener: Callback<Double>) {
        val token = sharedPreferences.getString(tokenKey, "")!!

        val rating = Rating(
            user.userId,
            rate
        )

        val call = request.rate(token, rating)
        call.enqueue(listener)
    }

    fun getRate(user: UserResponse, listener: Callback<Double>) {
        val token = sharedPreferences.getString(tokenKey, "")!!

        val call = request.getRate(token, user.userId)
        call.enqueue(listener)
    }

    fun updateIsHelper(listener: Callback<UserResponse>) {
        val token = sharedPreferences.getString(tokenKey, "")!!

        val call = request.updateIsHelper(token)
        call.enqueue(listener)
    }

    fun updateIsPhoneAvailable(listener: Callback<UserResponse>) {
        val token = sharedPreferences.getString(tokenKey, "")!!

        val call = request.updateIsPhoneAvailable(token)
        call.enqueue(listener)
    }

    fun getUser(userId: Long, listener: Callback<UserResponse>) {
        val token = sharedPreferences.getString(tokenKey, "")!!

        val call = request.getUser(token, userId)
        call.enqueue(listener)
    }
}
