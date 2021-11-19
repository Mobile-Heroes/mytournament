package com.mobile.heroes.mytournament.networking.services.NewUserResource

import com.mobile.heroes.mytournament.networking.Constants
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountRequest
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiNewUser {

    @POST(Constants.USER)
    fun postNewUser(@Header("Authorization") token: String, @Body account: NewUserRequest): Call<NewUserResponse>

    @GET(Constants.USER_ACCOUNT)
    fun getNewUser(@Header("Authorization") token: String): Response<NewUserResponse>

}