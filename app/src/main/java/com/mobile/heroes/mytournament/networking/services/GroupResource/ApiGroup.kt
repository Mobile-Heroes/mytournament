package com.mobile.heroes.mytournament.networking.services.GroupResource

import com.mobile.heroes.mytournament.networking.Constants

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiGroup {


    @POST(Constants.GROUP_URL)
    fun postGroup(@Header("Authorization") token: String, @Body group: GroupRequest): Call<GroupResponse>

    @GET(Constants.GROUP_URL)
    fun getGroup(@Header("Authorization") token: String): Response<GroupResponse>

    @GET("${Constants.GROUP_URL}/{id}")
    fun getOneGroup(@Header("Authorization") token: String,
                       @Path("id") id:String,): Response<GroupResponse>

    @PUT("${Constants.GROUP_URL}/{id}")
    fun updateGroup(@Header("Authorization") token: String,
                       @Path("id") id:String,
                       @Body group: GroupRequest
    ): Call<GroupResponse>

    @DELETE("${Constants.GROUP_URL}/{id}")
    fun deleteGroup(@Header("Authorization") token: String,
                       @Path("id") id:String,
                       @Body group: GroupRequest
    ): Call<GroupResponse>




}