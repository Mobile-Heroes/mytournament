package com.mobile.heroes.mytournament.networking.services

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

    //Login
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    //Get Account
    @GET(Constants.USER_ACCOUNT)
    fun getAccount(@Header("Authorization") token: String): Call<AccountResponce>

    //Favorites


    //Fields


    //Groups


    //Matches
    @POST(Constants.MATCH_URL)
    fun postMatch(@Header("Authorization") token: String, @Body match: MatchRequest): Call<MatchResponce>

    //Payments


    //Team Tournaments


    //Users


    //User Stats


}