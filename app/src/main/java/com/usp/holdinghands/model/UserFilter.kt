package com.usp.holdinghands.model

data class UserFilter(
    val gender: Gender,
    val ageMin: Int,
    val ageMax: Int,
    val distance: Double,
    val helpTypes: List<HelpType>
)
