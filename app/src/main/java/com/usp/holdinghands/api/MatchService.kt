package com.usp.holdinghands.api

import com.usp.holdinghands.model.MatchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface MatchService {

    @POST("/api/match/invite/{user_id_received}")
    fun sendInvite(@Header("Authorization") authorization: String, @Path("user_id_received") userIdReceived: Long): Call<MatchResponse>

    @GET("/api/match/{status}")
    fun getMatchs(@Header("Authorization") authorization: String, @Path("status") status: String): Call<List<MatchResponse>>

    @POST("/api/match/{status}/{match_id}")
    fun acceptRejectMatch(@Header("Authorization") authorization: String, @Path("status") status: String, @Path("match_id") matchId: Long): Call<MatchResponse>
}
