package com.mobile.heroes.mytournament.networking.services.FieldResource

import com.mobile.heroes.mytournament.networking.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceField {

    @POST(Constants.FIELD_URL)
    fun postMatch(@Header("Authorization") token: String, @Body field: FieldRequest): Call<FieldResponse>

    @GET(Constants.FIELD_URL)
    fun getMatch(@Header("Authorization") token: String): Response<FieldResponse>

    @GET("${Constants.FIELD_URL}/{id}")
    fun getOneMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<FieldResponse>

    @PUT("${Constants.FIELD_URL}/{id}")
    fun updateMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body field: FieldRequest
    ): Call<FieldResponse>

    @DELETE("${Constants.FIELD_URL}/{id}")
    fun deleteMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body field: FieldRequest
    ): Call<FieldResponse>
}