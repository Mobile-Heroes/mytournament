package com.mobile.heroes.mytournament.networking.services.PaymentResource

import com.mobile.heroes.mytournament.networking.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiPayment {


    @POST(Constants.PAYMENT_URL)
    fun postPayment(@Header("Authorization") token: String, @Body Payment: PaymentRequest): Call<PaymentResponse>

    @GET(Constants.PAYMENT_URL)
    fun getPayment(@Header("Authorization") token: String): Response<PaymentResponse>

    @GET("${Constants.PAYMENT_URL}/{id}")
    fun getOnePayment(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<PaymentResponse>

    @PUT("${Constants.PAYMENT_URL}/{id}")
    fun updatePayment(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body payment: PaymentRequest
    ): Call<PaymentResponse>

    @DELETE("${Constants.PAYMENT_URL}/{id}")
    fun deletePayment(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body payment: PaymentRequest
    ): Call<PaymentResponse>




}