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
import com.usp.holdinghands.utils.JsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.lang.reflect.Type

class UserController(val context: Context) {

    val gson = Gson()
    private val listUserType: Type = object : TypeToken<List<User>>() {}.type
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()

    val sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE)
    val userKey = "userInfo"

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

    fun setLoggedUser() {
        val user = User(
            1,
            "Heloise Cavalcante",
            35,
            10.0,
            mutableListOf(HelpType.TYPE_1, HelpType.TYPE_5),
            "heloise",
            Gender.FEMALE,
            "Desenvolvedora",
            4,
            "teste@teste.com",
            "(11) 12345 6789",
            "01/02/1995",
            "Rua dos Testes, São Paulo - São Paulo"
        )

        sharedPreferences.edit().putString(userKey, JsonUtil.toJson(user)).apply()
    }

    fun getLoggedUser(): User {
        //When integration with API is complete this logic will change to return an exception if there is no logged user
        setLoggedUser()

        val userString = sharedPreferences.getString(userKey, null)
        return if (userString != null) {
            JsonUtil.fromJson(userString)
        } else {
            throw Exception()
        }
    }

    suspend fun makeSearch(userFilter: UserFilter): List<User> {
        val users = getUsers()
        return users.filter { user ->
            shouldIncludeUser(userFilter, user)
        }
    }

    suspend fun getHelpRequests(): List<User> {
        val users = getUsers()
        return users.filter { it.sentRequest ?: false }
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
