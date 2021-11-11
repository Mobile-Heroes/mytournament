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
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldRequest
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginRequest
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginResponse
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentRequest
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

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

        apiClient.getApiService()
            .login(LoginRequest(username = "admin", password = "admin", rememberMe = false))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    //TODO Obtener el partido en proceso o finalizado desde la BD
                    //TODO Agregar los datos de fecha y hora desde  la BD
                    //TODO Agregar la imagen de los logos de los escudos de lo equipos del partido
                    //TODO En base al partido seleccionado, se deberá de agregar puntajes al equipo y al torneo (tabla general)
                    //TODO: ESTA VARA FUNCIONA LA VARA ES QUE PORQUE LOS POJOS DE TOURNAMENT Y FIELD NO TIENEN LOS CAMPOS
                    // NESESARIOS CORREJIT PARA EVITAR EL ERROR
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()

                    if (loginResponse?.id_token != null) {
                        sessionManager.saveAuthToken(loginResponse.id_token)
                    } else {
                        println("Error")
                    }
                }
            })

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
                FieldResponse(1,1.0,1.0,"name","status"),
                TournamentResponse("description","endDate","format","icon","iconContentType",1,8,8,"startDate","status"),
                "Canceled"
            )
            if(isNetworkConnected())
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
    /*
        apiClient.getApiService()
            .postmatch(token = "Bearer ${sessionManager.fetchAuthToken()}", bodyResponse)
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
            })*/
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