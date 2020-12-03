package com.usp.holdinghands.utils

import com.google.gson.Gson

object JsonUtil {

    val gson = Gson()

    inline fun <reified T> toJson(user: T): String {
        return gson.toJson(user, T::class.java)
    }

    inline fun <reified T> fromJson(jsonString: String): T {
        return gson.fromJson(jsonString, T::class.java)
    }
}
