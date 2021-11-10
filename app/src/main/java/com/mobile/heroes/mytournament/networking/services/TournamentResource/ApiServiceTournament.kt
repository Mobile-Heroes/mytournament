package com.mobile.heroes.mytournament.networking.services.TournamentResource

import com.mobile.heroes.mytournament.networking.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceTournament {

    @POST(Constants.TOURNAMENT_URL)
    fun postMatch(@Header("Authorization") token: String,
                  @Body tournament: TournamentRequest): Call<TournamentResponse>

    @GET(Constants.TOURNAMENT_URL)
    fun getMatch(@Header("Authorization") token: String): Response<TournamentResponse>

    @GET("${Constants.TOURNAMENT_URL}/{id}")
    fun getOneMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<TournamentResponse>

    @PUT("${Constants.TOURNAMENT_URL}/{id}")
    fun updateMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body tournament: TournamentRequest
    ): Call<TournamentResponse>

    @DELETE("${Constants.TOURNAMENT_URL}/{id}")
    fun deleteMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body tournament: TournamentRequest
    ): Call<TournamentResponse>
}