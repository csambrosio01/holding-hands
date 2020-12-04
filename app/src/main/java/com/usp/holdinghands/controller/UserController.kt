package com.usp.holdinghands.controller

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.usp.holdinghands.R
import com.usp.holdinghands.api.UserService
import com.usp.holdinghands.configurations.RetrofitBuilder
import com.usp.holdinghands.model.*
import com.usp.holdinghands.persistence.AppDatabase
import com.usp.holdinghands.persistence.dao.UserDao
import com.usp.holdinghands.utils.JsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Callback
import java.io.InputStreamReader
import java.lang.reflect.Type

class UserController(val context: Context) {

    val gson = Gson()
    private val listUserType: Type = object : TypeToken<List<User>>() {}.type
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()

    val sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE)
    val userKey = "userInfo"
    val tokenKey = "token"

    val request = RetrofitBuilder.buildService(UserService::class.java)

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
