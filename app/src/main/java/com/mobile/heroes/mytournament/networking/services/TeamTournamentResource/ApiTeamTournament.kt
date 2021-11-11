package com.mobile.heroes.mytournament.networking.services.TeamTournamentResource

import com.mobile.heroes.mytournament.networking.Constants

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiTeamTournament {


    @POST(Constants.TEAM_TOURNAMENT_URL)
    fun postTeamTournament(@Header("Authorization") token: String, @Body teamTournament: TeamTournamentRequest): Call<TeamTournamentResponse>

    @GET(Constants.TEAM_TOURNAMENT_URL)
    fun getTeamTournament(@Header("Authorization") token: String): Response<TeamTournamentResponse>

    @GET("${Constants.TEAM_TOURNAMENT_URL}/{id}")
    fun getOneTeamTournament(@Header("Authorization") token: String,
                      @Path("id") id:String,): Response<TeamTournamentResponse>

    @PUT("${Constants.TEAM_TOURNAMENT_URL}/{id}")
    fun updateTeamTournament(@Header("Authorization") token: String,
                      @Path("id") id:String,
                      @Body teamTournament: TeamTournamentRequest
    ): Call<TeamTournamentResponse>

    @DELETE("${Constants.TEAM_TOURNAMENT_URL}/{id}")
    fun deleteTeamTournament(@Header("Authorization") token: String,
                      @Path("id") id:String,
                      @Body teamTournament: TeamTournamentRequest
    ): Call<TeamTournamentResponse>





}