package com.usp.holdinghands.controller

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.usp.holdinghands.R
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.HelpType
import com.usp.holdinghands.model.User
import com.usp.holdinghands.model.UserFilter
import java.io.InputStreamReader
import java.lang.reflect.Type

class UserController(val context: Context) {

    private val gson = Gson()
    private val listUserType: Type = object : TypeToken<List<User>>() {}.type

    fun getUsers(): List<User> {
        return gson.fromJson(
            InputStreamReader(context.resources.openRawResource(R.raw.users_mock)),
            listUserType
        )
    }

    fun toJson(users: List<User>): String {
        return gson.toJson(users, listUserType)
    }

    fun toJsonUser(user: User): String {
        return gson.toJson(user, User::class.java)
    }

    fun fromJsonString(jsonString: String): List<User> {
        return gson.fromJson(jsonString, listUserType)
    }

    fun fromJsonStringUser(jsonString: String): User {
        return gson.fromJson(jsonString, User::class.java)
    }

    fun makeSearch(userFilter: UserFilter): List<User> {
        val users = getUsers()
        val a = users.filter { user ->
            shouldIncludeUser(userFilter, user)
        }
        return a
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
