package com.mobile.heroes.mytournament.networking

import com.mobile.heroes.mytournament.networking.services.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var apiService: ApiService

    fun getApiService(): ApiService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

}