package com.mobile.heroes.mytournament.networking

import com.mobile.heroes.mytournament.networking.services.LoginResource.ApiServiceLogin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    //Universal

    private lateinit var apiService: ApiServiceLogin

    fun getApiService(): ApiServiceLogin {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiServiceLogin::class.java)
        }

        return apiService
    }

}