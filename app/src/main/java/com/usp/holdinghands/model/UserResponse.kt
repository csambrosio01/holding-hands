package com.usp.holdinghands.model

import java.util.*

data class UserResponse(
    val userId: Long,
    val name: String,
    val age: Int,
    val distance: Double,
    val helpTypes: String?,
    val gender: Gender,
    val profession: String,
    val email: String,
    val phone: String,
    val isHelper: Boolean,
    val birth: Date,
    val imageId: String?,
    val rating: Double,
    val numberOfHelps: Int
)
