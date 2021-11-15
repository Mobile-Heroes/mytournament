package com.mobile.heroes.mytournament.networking.services.AccountResource

import com.mobile.heroes.mytournament.networking.Constants
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsRequest
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceAccount {

    @GET(Constants.USER_ACCOUNT)
    fun getAccount(@Header("Authorization") token: String): Response<AccountResponce>

    @POST(Constants.USER_STATS_URL)
    fun postAccount(@Header("Authorization") token: String, @Body account: AccountRequest): Call<AccountResponce>

}