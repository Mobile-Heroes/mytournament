package com.mobile.heroes.mytournament.networking.services.AccountResource

import com.mobile.heroes.mytournament.networking.Constants

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceAccount {

    @GET(Constants.USER_ACCOUNT)
    fun getAccount(@Header("Authorization") token: String): Response<AccountResponce>

}