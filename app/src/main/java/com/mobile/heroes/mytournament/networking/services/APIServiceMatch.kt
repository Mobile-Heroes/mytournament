package com.mobile.heroes.mytournament.networking.services

import com.mobile.heroes.mytournament.networking.pojos.MatchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface APIServiceMatch {

    /**
     * Interface que maneja los servicios, puede ser visto como un controlador
     */

    @POST
    suspend fun sendDataMacht(
        @Header("id_token") authToken: String,
        @Url url: String,
        @Body match: MatchResponse
    ): Response<MatchResponse>
}