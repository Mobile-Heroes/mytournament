package com.mobile.heroes.mytournament

import SessionManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class soccer_scoreboard : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    lateinit var tvDateTime: TextView
    lateinit var tvUbication: TextView
    lateinit var ivHomeTeam: ImageView
    lateinit var ivVisitorTeam: ImageView
    lateinit var tieScoreH: TextInputEditText
    lateinit var tieScoreV: TextInputEditText
    lateinit var btnCancel: Button
    lateinit var btnAccept: Button
    var pointH: Int = 0
    var pointV: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soccer_scoreboard)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        tvDateTime = findViewById(R.id.tvDateTime)
        tvUbication = findViewById(R.id.tvUbication)
        ivHomeTeam = findViewById(R.id.ivHomeTeam)
        ivVisitorTeam = findViewById(R.id.ivVisitorTeam)
        tieScoreH = findViewById(R.id.tieScoreH)
        tieScoreV = findViewById(R.id.tieScoreV)
        btnCancel = findViewById(R.id.btnCancel)
        btnAccept = findViewById(R.id.btnAccept)

        btnCancel.setOnClickListener {
            View.OnClickListener {
                TODO("RETURN TO THE MAIN MENU WITHOUT SAVING THE SCORE")
            }
        }

        btnAccept.setOnClickListener {
            try {
                pointH = tieScoreH.text.toString().toInt()
                pointV = tieScoreV.text.toString().toInt()
            } catch (nfe: NumberFormatException) {
                Toast.makeText(
                    applicationContext,
                    "Por favor escribir un número valido, sin caracteres especiales",
                    Toast.LENGTH_LONG
                ).show()
            }

            val bodyResponse: MatchRequest = MatchRequest(
                "2021-11-08T05:38:09.305Z",
                pointH,
                pointV,

                FieldResponse(
                    1,
                    1.0,
                    1.0,
                    "name",
                    "status"
                ),

                TournamentResponse(
                    "description",
                    "endDate",
                    "format",
                    "icon",
                    "iconContentType",
                    idUser = 1,
                    8,
                    8,
                    1,
                    "status",
                    "cancelado"
                ),

                "Canceled"
            )
            if (isNetworkConnected())
                sendScore(bodyResponse)
            else {
                Toast.makeText(
                    applicationContext,
                    "No hay conexión de internet en este momento, favor revisar su conexión o intente más tarde",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun sendScore(bodyResponse: MatchRequest) {

        var gson = Gson()
        var jsonString = gson.toJson(bodyResponse)
        println(jsonString)
        println(sessionManager.fetchAccount())

            apiClient.getApiService()
                .postMatch(token = "Bearer ${sessionManager.fetchAuthToken()}", bodyResponse)
                .enqueue(object : Callback<MatchResponce> {
                    override fun onFailure(call: Call<MatchResponce>, t: Throwable) {
                        println(call)
                        println(t)
                        println("Error")
                    }

                    override fun onResponse(
                        call: Call<MatchResponce>,
                        response: Response<MatchResponce>
                    ) {
                        runOnUiThread {
                            tieScoreH.refreshDrawableState()
                            tieScoreV.refreshDrawableState()
                        }
                    }
                })
    }

    /**
     * Este método checkea sí hay red
     */
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}