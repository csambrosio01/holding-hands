package com.usp.holdinghands.controller

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.usp.holdinghands.R
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.HelpType
import com.usp.holdinghands.model.User
import com.usp.holdinghands.model.UserFilter
import com.usp.holdinghands.persistence.AppDatabase
import com.usp.holdinghands.persistence.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.lang.reflect.Type

class UserController(val context: Context) {

    private val gson = Gson()
    private val listUserType: Type = object : TypeToken<List<User>>() {}.type
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        var value: List<User>
        value = userDao.getAll()
        if (value.isEmpty()) {
            value = gson.fromJson(
                InputStreamReader(context.resources.openRawResource(R.raw.users_mock)),
                listUserType
            )
            userDao.insert(value)
            value = userDao.getAll()
        }
        value.sortedBy { it.distance }
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

    suspend fun makeSearch(userFilter: UserFilter): List<User> {
        val users = getUsers()
        return users.filter { user ->
            shouldIncludeUser(userFilter, user)
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
