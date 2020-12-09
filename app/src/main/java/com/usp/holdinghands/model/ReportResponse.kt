package com.usp.holdinghands.model

class ReportResponse(
    val userReporter: UserResponse,
    val userReported: UserResponse,
    val message: String
)
