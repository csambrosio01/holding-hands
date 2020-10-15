package com.usp.holdinghands.model

import com.usp.holdinghands.App
import com.usp.holdinghands.R

enum class Gender {
    MALE, FEMALE, BOTH
}

enum class HelpType(val type: String) {
    TYPE_1(App.context!!.getString(R.string.filter_category_1_switch)),
    TYPE_2(App.context!!.getString(R.string.filter_category_2_switch)),
    TYPE_3(App.context!!.getString(R.string.filter_category_3_switch)),
    TYPE_4(App.context!!.getString(R.string.filter_category_4_switch)),
    TYPE_5(App.context!!.getString(R.string.filter_category_5_switch)),
    ALL(App.context!!.getString(R.string.filter_category_6_switch))
}

data class User(
    val name: String,
    val age: Int,
    val distance: Double,
    val helpTypes: List<HelpType>,
    val image: String,
    val gender: Gender
) {
    override fun toString(): String {
        return "{name: ${this.name}, age: ${this.age}, name: ${this.distance}, name: ${this.distance}, helpTypes: ${this.getHelpAsString()}, image: ${this.image}, gender: ${this.gender}}"
    }
}

fun User.getHelpAsString(): String {
    val sb = StringBuilder()
    sb.append(App.context?.resources?.getText(R.string.user_help_types_header))
    for (value in helpTypes) {
        sb.append(" - ${value.type}\n")
    }
    return sb.toString().trim()
}
