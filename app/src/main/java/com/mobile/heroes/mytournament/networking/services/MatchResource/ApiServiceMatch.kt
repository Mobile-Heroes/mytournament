package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.mobile.heroes.mytournament.networking.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceMatch {

    @POST(Constants.MATCH_URL)
    fun postMatch(@Header("Authorization") token: String, @Body match: MatchRequest): Call<MatchResponce>

    @GET(Constants.MATCH_URL)
    fun getMatch(@Header("Authorization") token: String): Response<MatchResponce>

    @GET("${Constants.MATCH_URL}/{id}")
    fun getOneMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<MatchResponce>

    @PUT("${Constants.MATCH_URL}/{id}")
    fun updateMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body match: MatchRequest): Call<MatchResponce>

    @DELETE("${Constants.MATCH_URL}/{id}")
    fun deleteMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body match: MatchRequest): Call<MatchResponce>
}