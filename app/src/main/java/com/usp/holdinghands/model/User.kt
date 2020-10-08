package com.usp.holdinghands.model

import com.usp.holdinghands.App
import com.usp.holdinghands.R
import java.lang.StringBuilder

data class User(val name: String, val age: Int, val distance: String, val helpTypes: List<String>, val image: String)

fun User.getHelpAsString(): String {
    val sb = StringBuilder()
    sb.append(App.context?.resources?.getText(R.string.user_help_types_header))
    for (value in helpTypes) {
        sb.append(" - $value\n")
    }
    return sb.toString().trim()
}

const val TAG_USERS = "users"
const val TAG_NAME = "name"
const val TAG_AGE = "age"
const val TAG_DISTANCE = "distance"
const val TAG_HELP_TYPES = "helpTypes"
const val TAG_IMAGE = "image"
