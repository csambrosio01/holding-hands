package com.usp.holdinghands.controller

import android.content.Context
import android.content.SharedPreferences
import com.usp.holdinghands.api.MatchService
import com.usp.holdinghands.configurations.RetrofitBuilder
import com.usp.holdinghands.model.MatchResponse
import com.usp.holdinghands.model.MatchStatus
import retrofit2.Callback

class MatchController(val context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE)

    val request = RetrofitBuilder.buildService(MatchService::class.java)

    fun sendInvite(userIdReceived: Long, listener: Callback<MatchResponse>) {
        val token = sharedPreferences.getString(tokenKey, "")!!
        val call = request.sendInvite(token, userIdReceived)
        call.enqueue(listener)
    }

    fun getMatchs(status: MatchStatus, listener: Callback<List<MatchResponse>>) {
        val token = sharedPreferences.getString(tokenKey, "")!!
        val call = request.getMatchs(token, status.name)
        call.enqueue(listener)
    }

    fun acceptRejectMatch(status: MatchStatus, matchId: Long, listener: Callback<MatchResponse>) {
        val token = sharedPreferences.getString(tokenKey, "")!!
        val call = request.acceptRejectMatch(token, status.name, matchId)
        call.enqueue(listener)
    }
}
