package com.usp.holdinghands.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.usp.holdinghands.App
import com.usp.holdinghands.R
import com.usp.holdinghands.model.entities.BaseEntity

enum class Gender {
    MALE, FEMALE, BOTH
}

enum class HelpType(val type: String) {
    TYPE_1(App.context!!.getString(R.string.help_type_1)),
    TYPE_2(App.context!!.getString(R.string.help_type_2)),
    TYPE_3(App.context!!.getString(R.string.help_type_3)),
    TYPE_4(App.context!!.getString(R.string.help_type_4)),
    TYPE_5(App.context!!.getString(R.string.help_type_5)),
    ALL(App.context!!.getString(R.string.help_type_all))
}

class UserConverters {
    private inline fun <T : Enum<T>> T.toInt(): Int = this.ordinal

    private inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]

    @TypeConverter
    fun genderToTnt(value: Gender) = value.toInt()
    @TypeConverter
    fun intToGender(value: Int) = value.toEnum<Gender>()

    @TypeConverter
    fun storedStringToHelpType(value: String): List<HelpType>? {
        val dbValues: List<String> =
            value.split("\\s*,\\s*".toRegex()).toList()
        val enums: MutableList<HelpType> = ArrayList()
        for (s in dbValues) enums.add(HelpType.valueOf(s))
        return enums
    }

    @TypeConverter
    fun helpTypesToStoredString(cl: List<HelpType>): String? {
        var value = ""
        for (helpType in cl) value += helpType.name + ","
        val a = value.dropLast(1)
        return a
    }
}

@Entity(tableName = "user")
data class User(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    val name: String,
    val age: Int,
    val distance: Double,
    val helpTypes: List<HelpType>,
    val image: String,
    val gender: Gender,
    val profession: String,
    val numberOfHelps: Int,
    val email: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val address: String? = null,
    val sentRequest: Boolean? = null,
    val rating: Double = 5.0
) : BaseEntity() {
    override fun toString(): String {
        return "{name: ${this.name}, age: ${this.age}, name: ${this.distance}, name: ${this.distance}, helpTypes: ${this.getHelpAsString()}, image: ${this.image}, gender: ${this.gender}, profession: ${this.profession}, numberOfHelps: ${this.numberOfHelps}}"
    }
}

fun User.getHelpAsString(withHeader: Boolean = true): String {
    val sb = StringBuilder()
    if (withHeader) sb.append(App.context?.resources?.getText(R.string.user_help_types_header))
    for (value in helpTypes) {
        sb.append("- ${value.type}\n")
    }
    return sb.toString().trim()
}
