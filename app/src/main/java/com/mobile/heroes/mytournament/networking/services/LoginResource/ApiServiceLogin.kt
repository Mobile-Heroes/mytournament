package com.mobile.heroes.mytournament.networking.services.LoginResource

import com.mobile.heroes.mytournament.networking.Constants
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginRequest
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginResponse
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServiceLogin {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET(Constants.USER_ACCOUNT)
    fun getAccount(@Header("Authorization") token: String): Call<AccountResponce>
}