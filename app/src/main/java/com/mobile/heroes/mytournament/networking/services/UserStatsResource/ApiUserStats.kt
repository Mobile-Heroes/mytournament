package com.mobile.heroes.mytournament.networking.services.UserStatsResource

import com.mobile.heroes.mytournament.networking.Constants

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiUserStats {

    @POST(Constants.USER_STATS_URL)
    fun postUserStats(@Header("Authorization") token: String, @Body userStats: UserStatsRequest): Call<UserStatsResponse>

    @GET(Constants.USER_STATS_URL)
    fun getUserStats(@Header("Authorization") token: String): Response<UserStatsResponse>

    @GET("${Constants.USER_STATS_URL}/{id}")
    fun getOneUserStats(@Header("Authorization") token: String,
                             @Path("id") id:String,): Response<UserStatsResponse>

    @PUT("${Constants.USER_STATS_URL}/{id}")
    fun updateUserStats(@Header("Authorization") token: String,
                             @Path("id") id:String,
                             @Body userStats: UserStatsRequest
    ): Call<UserStatsResponse>

    @DELETE("${Constants.USER_STATS_URL}/{id}")
    fun deleteUserStats(@Header("Authorization") token: String,
                             @Path("id") id:String,
                             @Body userStats: UserStatsRequest
    ): Call<UserStatsResponse>





}