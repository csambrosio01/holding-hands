package com.usp.holdinghands.utils

import com.usp.holdinghands.App
import com.usp.holdinghands.R
import com.usp.holdinghands.model.HelpType

object EnumConverter {

    inline fun <reified T : Enum<T>> stringToEnumList(value: String): List<T> {
        val dbValues: List<String> =
            value.split("\\s*,\\s*".toRegex()).toList()
        val enums: MutableList<T> = ArrayList()
        for (s in dbValues) enums.add(enumValueOf(s))
        return enums
    }

    fun <T : Enum<T>> enumListToString(cl: List<T>): String {
        var value = ""
        for (enum in cl) value += enum.name + ","
        return value.dropLast(1)
    }

    fun getHelpAsString(helpTypes: List<HelpType>, withHeader: Boolean = true): String {
        val sb = StringBuilder()
        if (withHeader) sb.append(App.context?.resources?.getText(R.string.user_help_types_header))
        for (value in helpTypes) {
            sb.append("- ${value.type}\n")
        }
        return sb.toString().trim()
    }
}
