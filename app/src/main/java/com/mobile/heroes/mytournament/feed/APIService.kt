package com.mobile.heroes.mytournament.feed

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getFeed(@Url url:String): Response<FeedResponse>
}