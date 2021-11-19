package com.mobile.heroes.mytournament.signup



import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.mobile.heroes.mytournament.LoadingScreen
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.UserResource.UserRequest
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse

import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsRequest
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import kotlinx.android.synthetic.main.activity_complete_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import SessionManager
import com.mobile.heroes.mytournament.MainActivity


class CompleteRegistration : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_registration)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        val account = sessionManager.fetchAccount()


        btnSelect.setOnClickListener {
            selectImage()
        }

        btnSubmit.setOnClickListener {
            if (imageViewLogo.getDrawable() == null) {
                Toast.makeText(
                        applicationContext,
                        "Debe seleccionar una imagen",
                        Toast.LENGTH_LONG
                ).show()
            } else {


                var logo = (imageViewLogo.drawable as BitmapDrawable).bitmap
                var logoApp: String?
                logoApp = logo.toBase64String();

                val bodyResponse = UserStatsRequest(
                        0,
                        logoApp,
                        "image/png",
                        UserResponse(id = account!!.id),
                        account!!.firstName,
                        0,
                        0
                )
                if (isNetworkConnected()) {
                    sendUserStats(bodyResponse)
                } else {
                    Toast.makeText(
                            applicationContext,
                            "No hay conexión de internet en este momento, favor revisar su conexión o intente más tarde",
                            Toast.LENGTH_LONG
                    ).show()
                }

            }

        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageViewLogo.setImageURI(data?.data) // handle chosen image

    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    private fun sendUserStats(bodyResponse: UserStatsRequest) {
        LoadingScreen.displayLoadingWithText(this,"Por favor espere",false)

        apiClient.getApiService()
                .postUserStats(token = "Bearer ${sessionManager.fetchAuthToken()}", bodyResponse)
                .enqueue(object : Callback<UserStatsResponse> {
                    override fun onFailure(call: Call<UserStatsResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Ocurrió un error en la solicitud!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                            call: Call<UserStatsResponse>,
                            response: Response<UserStatsResponse>

                    ) {
                        if(response.code() == 201){
                            runOnUiThread {
                                sessionManager.saveUserStats(response.body()!!)
                                Toast.makeText(applicationContext, "Información recibida correctamente", Toast.LENGTH_SHORT).show()
                                LoadingScreen.hideLoading()
                                Thread.sleep(2000)
                                val easyIntent: Intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(easyIntent)
                                finish()
                            }

                        } else {
                            print(response.code())
                            Toast.makeText(applicationContext, "Ha ocurrido un error en la solicitud", Toast.LENGTH_SHORT).show()
                            LoadingScreen.hideLoading()

                        }

                    }
                })
    }


    fun Bitmap.toBase64String(): String {
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 10, this)
            return Base64.encodeToString(toByteArray(), Base64.DEFAULT)
        }
    }
}