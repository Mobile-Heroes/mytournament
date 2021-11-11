package com.mobile.heroes.mytournament.networking.services.FavoriteResource

import com.mobile.heroes.mytournament.networking.Constants

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiFavorite {

    @POST(Constants.FAVORITE_URL)
    fun postFavorite(@Header("Authorization") token: String, @Body favorite: FavoriteRequest): Call<FavoriteResponse>

    @GET(Constants.FAVORITE_URL)
    fun getFavorite(@Header("Authorization") token: String): Response<FavoriteResponse>

    @GET("${Constants.FAVORITE_URL}/{id}")
    fun getOneFavorite(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<FavoriteResponse>

    @PUT("${Constants.FAVORITE_URL}/{id}")
    fun updateFavorite(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body favorite: FavoriteRequest
    ): Call<FavoriteResponse>

    @DELETE("${Constants.FAVORITE_URL}/{id}")
    fun deleteFavorite(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body favorite: FavoriteRequest
    ): Call<FavoriteResponse>

}