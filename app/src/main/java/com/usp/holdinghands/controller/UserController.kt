package com.usp.holdinghands.controller

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.usp.holdinghands.R
import com.usp.holdinghands.model.User
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

    fun fromJsonString(jsonString: String): List<User> {
        return gson.fromJson(jsonString, listUserType)
    }
}
