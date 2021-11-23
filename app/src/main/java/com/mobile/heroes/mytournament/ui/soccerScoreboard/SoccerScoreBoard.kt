package com.mobile.heroes.mytournament.ui.soccerScoreboard

import SessionManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.mobile.heroes.mytournament.LoadingScreen
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.Tournament
import com.mobile.heroes.mytournament.helpers.MatchRequestDTO
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

class
SoccerScoreBoard : AppCompatActivity() {

    //Networking variables
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    //Visual components variables
    private lateinit var tvDateTime: TextView
    private lateinit var tvUbication: TextView
    private lateinit var ivHomeTeam: ImageView
    private lateinit var ivVisitorTeam: ImageView
    private lateinit var tieScoreH: TextInputEditText
    private lateinit var tieScoreV: TextInputEditText
    private lateinit var btnCancel: Button
    private lateinit var btnAccept: Button
    private var pointH: Int = 0
    private var pointV: Int = 0

    //DTO entities
    private var matchDTO: MatchRequestDTO? = null
    private var fieldDTO: FieldResponse? = null
    private var teamTournamentHome: TeamTournamentResponse? = null
    private var teamTournamentVisitor: TeamTournamentResponse? = null
    private var userStatsHome: UserStatsResponse? = null
    private var userStatsVisitor: UserStatsResponse? = null
    private var matchSender: MatchRequestDTO? = null

    //Intent Variables
    private var idMatch: String? = ""

    //Date variables
    private lateinit var datetime: Date
    private lateinit var dateDisplayed: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soccer_scoreboard)

        //Initialization of networking variables
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        //Initialization of visual components variables
        tvDateTime = findViewById(R.id.tvDateTime)
        tvUbication = findViewById(R.id.tvUbication)
        ivHomeTeam = findViewById(R.id.ivHomeTeam)
        ivVisitorTeam = findViewById(R.id.ivVisitorTeam)
        tieScoreH = findViewById(R.id.tieScoreH)
        tieScoreV = findViewById(R.id.tieScoreV)
        btnCancel = findViewById(R.id.btnCancel)
        btnAccept = findViewById(R.id.btnAccept)

        btnCancel.setOnClickListener {
            val intent = Intent(applicationContext, Tournament::class.java)
            startActivity(intent)
            finish()
        }

        idMatch = getIntent().getStringExtra("id")

        //DTO Initialization variables and workflow
        //Get Match
        apiClient.getApiService()
            .getOneMatch(token = "Bearer ${sessionManager.fetchAuthToken()}", id = idMatch!!)
            .enqueue(object : Callback<MatchRequestDTO> {
                override fun onFailure(call: Call<MatchRequestDTO>, t: Throwable) {
                    println(call)
                    println(t)
                    println("Error")
                }

                override fun onResponse(
                    call: Call<MatchRequestDTO>,
                    response: Response<MatchRequestDTO>
                ) {
                    if (response.code() == 200) {
                        runOnUiThread {
                            matchDTO = response.body()!!

                            println("Tengo el partido")

                            matchSender = matchDTO

                            apiClient.getApiService()
                                .getOneField(
                                    token = "Bearer ${sessionManager.fetchAuthToken()}",
                                    id = matchDTO!!.idField?.id.toString()
                                )
                                .enqueue(object : Callback<FieldResponse> {
                                    override fun onResponse(
                                        call: Call<FieldResponse>,
                                        response: Response<FieldResponse>
                                    ) {
                                        if (response.code() == 200) {
                                            println("Tengo la cancha")
                                            runOnUiThread {
                                                var date = ZonedDateTime.parse(matchDTO!!.date.toString())
                                                datetime = Date.from(date.toInstant())
                                                dateDisplayed = datetime.dateToString("dd / MMM / yyyy")
                                                fieldDTO = response.body()!!
                                                tvUbication.setText(fieldDTO!!.name.toString())
                                                tvDateTime.setText(dateDisplayed)
                                            }
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<FieldResponse>,
                                        t: Throwable
                                    ) {
                                        println(call)
                                        println(t)
                                        println("Error")
                                    }

                                })

                            //Get both Team Tournaments id (home & visitor) from the match
                            apiClient.getApiService()
                                .getOneTeamTournament(
                                    token = "Bearer ${sessionManager.fetchAuthToken()}",
                                    id = matchDTO?.idTeamTournamentHome?.id.toString()
                                )
                                .enqueue(object : Callback<TeamTournamentResponse> {
                                    override fun onFailure(
                                        call: Call<TeamTournamentResponse>,
                                        t: Throwable
                                    ) {
                                        println(call)
                                        println(t)
                                        println("Error")
                                    }

                                    override fun onResponse(
                                        call: Call<TeamTournamentResponse>,
                                        response: Response<TeamTournamentResponse>
                                    ) {
                                        if (response.code() == 200) {
                                            println("Tengo el equipo casa")
                                            runOnUiThread {
                                                teamTournamentHome = response.body()!!

                                                apiClient.getApiService()
                                                    .getOneUserStats(
                                                        token = "Bearer ${sessionManager.fetchAuthToken()}",
                                                        id = teamTournamentHome?.idUser?.id!!.toInt()
                                                    )
                                                    .enqueue(object :
                                                        Callback<UserStatsResponse> {
                                                        override fun onFailure(
                                                            call: Call<UserStatsResponse>,
                                                            t: Throwable
                                                        ) {
                                                            println(call)
                                                            println(t)
                                                            println("Error")
                                                        }

                                                        override fun onResponse(
                                                            call: Call<UserStatsResponse>,
                                                            response: Response<UserStatsResponse>
                                                        ) {
                                                            if (response.code() == 200) {
                                                                runOnUiThread {
                                                                    println("Tengo user stats")
                                                                    userStatsHome =
                                                                        response.body()!!
                                                                    val imageBytesHome =
                                                                        Base64.decode(
                                                                            userStatsHome?.icon.toString(),
                                                                            0
                                                                        )
                                                                    val imageHome =
                                                                        BitmapFactory.decodeByteArray(
                                                                            imageBytesHome,
                                                                            0,
                                                                            imageBytesHome.size
                                                                        )
                                                                    ivHomeTeam.setImageBitmap(
                                                                        imageHome
                                                                    )
                                                                }

                                                            } else {
                                                                runOnUiThread {
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Hubo un error en el envío de datos",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }

                                                            }

                                                        }
                                                    })

                                            }

                                        } else {
                                            runOnUiThread {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Hubo un error en el envío de datos",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        }

                                    }
                                })

                            apiClient.getApiService()
                                .getOneTeamTournament(
                                    token = "Bearer ${sessionManager.fetchAuthToken()}",
                                    id = matchDTO?.idTeamTournamentVisitor?.id.toString()
                                )
                                .enqueue(object : Callback<TeamTournamentResponse> {
                                    override fun onFailure(
                                        call: Call<TeamTournamentResponse>,
                                        t: Throwable
                                    ) {
                                        println(call)
                                        println(t)
                                        println("Error")
                                    }

                                    override fun onResponse(
                                        call: Call<TeamTournamentResponse>,
                                        response: Response<TeamTournamentResponse>
                                    ) {
                                        if (response.code() == 200) {
                                            println("Tengo el equipo visitante")
                                            runOnUiThread {
                                                teamTournamentVisitor = response.body()!!

                                                apiClient.getApiService()
                                                    .getOneUserStats(
                                                        token = "Bearer ${sessionManager.fetchAuthToken()}",
                                                        id = teamTournamentVisitor?.idUser?.id!!.toInt()
                                                    )
                                                    .enqueue(object :
                                                        Callback<UserStatsResponse> {
                                                        override fun onFailure(
                                                            call: Call<UserStatsResponse>,
                                                            t: Throwable
                                                        ) {
                                                            println(call)
                                                            println(t)
                                                            println("Error")
                                                        }

                                                        override fun onResponse(
                                                            call: Call<UserStatsResponse>,
                                                            response: Response<UserStatsResponse>
                                                        ) {
                                                            if (response.code() == 200) {
                                                                runOnUiThread {
                                                                    println("Tengo user stats de los visitantes")
                                                                    userStatsVisitor =
                                                                        response.body()!!

                                                                    val imageBytesVisitor =
                                                                        Base64.decode(
                                                                            userStatsVisitor?.icon.toString(),
                                                                            0
                                                                        )
                                                                    val imageVisitor =
                                                                        BitmapFactory.decodeByteArray(
                                                                            imageBytesVisitor,
                                                                            0,
                                                                            imageBytesVisitor.size
                                                                        )
                                                                    ivVisitorTeam.setImageBitmap(
                                                                        imageVisitor
                                                                    )
                                                                }

                                                            } else {
                                                                runOnUiThread {
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Hubo un error en el envío de datos",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }

                                                            }

                                                        }
                                                    })

                                            }

                                        } else {
                                            runOnUiThread {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Hubo un error en la transferencia de datos",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        }

                                    }
                                })
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Hubo un error en el envío de datos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }
            })

        tieScoreH.setOnClickListener {
            tieScoreH.text?.clear()
        }

        tieScoreV.setOnClickListener {
            tieScoreV.refreshDrawableState()
        }

        btnAccept.setOnClickListener {

            try {
                pointH = tieScoreH.text.toString().toInt()
                pointV = tieScoreV.text.toString().toInt()
                matchSender?.goalsHome = pointH
                matchSender?.goalsAway = pointV
                matchSender?.status = "Complete"
                if (isNetworkConnected()) {
                    sendScore(matchSender!!)
                    sendTeamTournamentsUpdate(matchSender!!, teamTournamentHome!!, teamTournamentVisitor!!)
                }
                else {
                    Toast.makeText(
                        applicationContext,
                        "No hay conexión de internet en este momento, favor revisar su conexión o intente más tarde",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (nfe: NumberFormatException) {
                Toast.makeText(
                    applicationContext,
                    "Por favor escribir un número valido, sin caracteres especiales",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun sendTeamTournamentsUpdate(
        matchSender: MatchRequestDTO,
        teamTournamentHome: TeamTournamentResponse,
        teamTournamentVisitor: TeamTournamentResponse
    ) {
        if (matchSender.goalsHome!! > matchSender.goalsAway!!) {
            teamTournamentHome.points = teamTournamentHome.points?.plus(3)
            teamTournamentHome.goalsDone = teamTournamentHome.goalsDone?.plus(pointH)
            teamTournamentHome.goalsReceived = teamTournamentHome.goalsReceived?.plus(pointV)
            teamTournamentVisitor.goalsDone = teamTournamentVisitor.goalsDone?.plus(pointV)
            teamTournamentVisitor.goalsReceived = teamTournamentVisitor.goalsReceived?.plus(pointH)
        }
        else {
            teamTournamentVisitor.points = teamTournamentVisitor.points?.plus(3)
            teamTournamentVisitor.goalsDone = teamTournamentVisitor.goalsDone?.plus(pointV)
            teamTournamentVisitor.goalsReceived = teamTournamentVisitor.goalsReceived?.plus(pointH)
            teamTournamentHome.goalsDone = teamTournamentHome.goalsDone?.plus(pointH)
            teamTournamentHome.goalsReceived = teamTournamentHome.goalsReceived?.plus(pointV)
        }

        if(matchSender.goalsHome!! == matchSender.goalsAway!!) {
            teamTournamentHome.points = teamTournamentHome.points?.plus(1)
            teamTournamentVisitor.points = teamTournamentVisitor.points?.plus(1)
            teamTournamentHome.goalsReceived = teamTournamentHome.goalsReceived?.plus(1)
            teamTournamentVisitor.goalsReceived = teamTournamentVisitor.goalsReceived?.plus(1)
        }

        LoadingScreen.displayLoadingWithText(this,"Por favor espere", false)

        apiClient.getApiService()
            .updateTeamTournament(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                teamTournamentHome.id.toString(),
                teamTournamentHome
            )
            .enqueue(object : Callback<TeamTournamentResponse>{
                override fun onResponse(
                    call: Call<TeamTournamentResponse>,
                    response: Response<TeamTournamentResponse>
                ) {
                    println(response.code())
                    println("Se envió el dato al team tournament 1")
                }

                override fun onFailure(call: Call<TeamTournamentResponse>, t: Throwable) {
                    println(call)
                    println(t)
                    println("Error")
                }

            })

        apiClient.getApiService()
            .updateTeamTournament(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                teamTournamentVisitor.id.toString(),
                teamTournamentVisitor
            )
            .enqueue(object : Callback<TeamTournamentResponse>{
                override fun onResponse(
                    call: Call<TeamTournamentResponse>,
                    response: Response<TeamTournamentResponse>
                ) {
                    println(response.code())
                    println("Se envió el dato al team tournament 2")
                    runOnUiThread {
                        Thread.sleep(2000)
                        Toast.makeText(
                            applicationContext,
                            "Dato enviado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        LoadingScreen.hideLoading()
                        val intent = Intent(applicationContext, Tournament::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<TeamTournamentResponse>, t: Throwable) {
                    println(call)
                    println(t)
                    println("Error")
                }

            })
    }

    private fun sendScore(bodyResponse: MatchRequestDTO) {

        val gson = Gson()
        val jsonString = gson.toJson(bodyResponse)
        println(jsonString)

        apiClient.getApiService()
            .updateMatch(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                id = bodyResponse.id.toString(),
                bodyResponse
            )
            .enqueue(object : Callback<MatchRequestDTO> {
                override fun onFailure(call: Call<MatchRequestDTO>, t: Throwable) {
                    println(call)
                    println(t)
                    println("Error")
                }

                override fun onResponse(
                    call: Call<MatchRequestDTO>,
                    response: Response<MatchRequestDTO>
                ) {
                    println(response.code())
                    if (response.code() == 200) {
                        println(response.code())
                        println("Se envió el dato al match")
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Hubo un error en el envío de datos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }
            })
    }

    private fun Date.dateToString(format:String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(this)
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