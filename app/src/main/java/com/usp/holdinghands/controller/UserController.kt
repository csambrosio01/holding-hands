package com.usp.holdinghands.controller

import android.content.Context
import android.location.Location
import com.usp.holdinghands.api.UserService
import com.usp.holdinghands.configurations.RetrofitBuilder
import com.usp.holdinghands.model.*
import com.usp.holdinghands.utils.JsonUtil
import retrofit2.Callback

class UserController(val context: Context) {

    val sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE)
    val userKey = "userInfo"
    val tokenKey = "token"

    val request = RetrofitBuilder.buildService(UserService::class.java)

    fun getUsers(location: Location, listener: Callback<List<UserResponse>>) {
        val token = sharedPreferences.getString(tokenKey, "")!!
        val call = request.getUsers(token, Location(location.latitude, location.longitude))
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

    fun getLoggedUser(): LoggedUser? {
        val userString = sharedPreferences.getString(userKey, null)
        return if (userString != null) {
            JsonUtil.fromJson(userString)
        } else {
            null
        }
    }

    private fun shouldIncludeUser(userFilter: UserFilter, user: User): Boolean {
        var isValid = true

        if (userFilter.gender != Gender.BOTH) {
            if (user.gender != userFilter.gender) isValid = false
        }

        if (user.age < userFilter.ageMin || user.age > userFilter.ageMax) isValid = false

        if (user.distance > userFilter.distance) isValid = false

        var contains = false
        if (!userFilter.helpTypes.contains(HelpType.ALL)) {
            for (helpType in user.helpTypes) {
                if (userFilter.helpTypes.contains(helpType)) contains = true
            }
            if (!contains) isValid = false
        }

        return isValid
    }
}
