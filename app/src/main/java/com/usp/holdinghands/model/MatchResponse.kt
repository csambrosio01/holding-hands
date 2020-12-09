package com.usp.holdinghands.model

enum class MatchStatus {
    PENDING, ACCEPT, DONE, REJECT, HISTORY
}

data class MatchResponse(
    val matchId: Long,
    val userSent: UserResponse,
    val userReceived: UserResponse,
    val status: MatchStatus = MatchStatus.PENDING
)
