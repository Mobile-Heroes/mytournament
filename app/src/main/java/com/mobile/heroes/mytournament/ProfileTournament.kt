package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentRequest
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentProfileTeamAdapter
import kotlinx.android.synthetic.main.activity_create_tournament.*
import kotlinx.android.synthetic.main.tournament_profile_body.*
import kotlinx.android.synthetic.main.tournament_profile_head_bottom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileTournament : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    private var teamTournamentList = mutableListOf<TeamTournamentResponse>()
    private var tournamentProfileTeamList = mutableListOf<TeamTournamentResponse>()

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var apiClient: ApiClient

    private var profileName: Any? = ""
    private var profileIcon: Any? = ""
    private var profileDescription: Any? = ""
    private var profileStartDate: Any? = ""
    private var profileFormat: Any? = ""
    private var profileId: Any? = ""
    private var profileParticipants: Any? = ""
    private var profileMatches: Any? = ""
    private var profileStatus: Any? = ""
    private var checkIfJoinedAleady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        loadIntentExtras()
        backbutton()
        bottomNavigationMenu()

        getTeamTournaments()




        tournamentProfileTeamAdapter =
            TournamentProfileTeamAdapter(tournamentProfileList, profileId.toString().toInt())
        rv_tournament_profile_teams.layoutManager = LinearLayoutManager(this)
        rv_tournament_profile_teams.adapter = tournamentProfileTeamAdapter
    }

    private fun loadIntentExtras() {
        val bundle = intent.extras
        profileName = bundle?.get("INTENT_NAME")
        profileIcon = bundle?.get("INTENT_ICON")
        profileDescription = bundle?.get("INTENT_DESCRIPTION")
        profileStartDate = bundle?.get("INTENT_START_DATE")
        profileFormat = bundle?.get("INTENT_FORMAT")
        profileId = bundle?.get("INTENT_ID")
        profileParticipants = bundle?.get("INTENT_PARTICIPANTS")
        profileMatches = bundle?.get("INTENT_MATCHES")
        profileStatus = bundle?.get("INTENT_STATUS")
    }

    fun changeTournamentProfileInfo() {
        var profileNameTextView: TextView = findViewById(R.id.tv_tournament_profile_name)
        profileNameTextView.setText("$profileName")

        var profileDescriptionTextView: TextView =
            findViewById(R.id.tv_tournament_profile_description)
        profileDescriptionTextView.setText("$profileDescription")

        var profileStartDateTextView: TextView = findViewById(R.id.tv_tournament_profile_date_value)
        profileStartDateTextView.setText("$profileStartDate")

        var profileParticipantsTextView: TextView =
            findViewById(R.id.tv_tournament_profile_participants_value)
        profileParticipantsTextView.setText("$profileParticipants")

        val imageBytes = Base64.decode("$profileIcon", 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        var profileIconImageView: ImageView = findViewById(R.id.iv_tournament_head_image)
        profileIconImageView.setImageBitmap(image)
    }

    private fun profileButtonStatusOptions() {
        val accountRole = sessionManager.fetchAccount()?.authorities?.get(0)

        if (accountRole == "ROLE_USER") {
            if (profileStatus == "Active") {
                favoriteButtonActions()
            }
            if (profileStatus == "InProgress") {
                joinTournamentButtonActions()
            }
        } else {
            var profileActionButton: Button = findViewById(R.id.bt_tournament_profile_action)
            profileActionButton.setText("Creador")
            profileActionButton.setBackgroundColor(resources.getColor(R.color.gris))
            profileActionButton.setTextColor(resources.getColor(R.color.black))
        }

    }

    private fun favoriteButtonActions() {
        var profileActionButton: Button = findViewById(R.id.bt_tournament_profile_action)
        profileActionButton.setText("Favorito")
        profileActionButton.setBackgroundColor(resources.getColor(R.color.verde))
        profileActionButton.setTextColor(resources.getColor(R.color.white))

        profileActionButton.setOnClickListener {

            val buttonText = profileActionButton.getText().toString()

            if (buttonText == "Favorito") {
                profileActionButton.setText("Siguiendo")
                profileActionButton.setBackgroundColor(resources.getColor(R.color.gris))
                profileActionButton.setTextColor(resources.getColor(R.color.black))
            }

            if (buttonText == "Siguiendo") {
                profileActionButton.setText("Favorito")
                profileActionButton.setBackgroundColor(resources.getColor(R.color.verde))
                profileActionButton.setTextColor(resources.getColor(R.color.white))

            }

        }

    }

    //Front end del boton

    private fun joinTournamentButtonActions() {
        var profileActionButton: Button = findViewById(R.id.bt_tournament_profile_action)

        val userStats = sessionManager.fetchUserStats()
        val userId = userStats?.id

        for (i: Int in 0..tournamentProfileList.size - 1) {
            //Leave tournament
            if (tournamentProfileList[i].id == userId) {
                checkIfJoinedAleady = true
                profileActionButton.setText("Abandonar")
                profileActionButton.setBackgroundColor(resources.getColor(R.color.cafe))
                profileActionButton.setTextColor(resources.getColor(R.color.white))
                //Backend to leave tournament

//                profileActionButton.setOnClickListener {
//                    leaveTheTournament()
//                }
            }
        }
        if (checkIfJoinedAleady == false) {

            profileActionButton.setText("Unirse")
            profileActionButton.setBackgroundColor(resources.getColor(R.color.verde))
            profileActionButton.setTextColor(resources.getColor(R.color.white))

            profileActionButton.setOnClickListener {
                joinToTournament()
            }
        }
    }

    private fun leaveTheTournament() {
        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        //Just need the account, that´s mean, we have the team id
        val account: AccountResponce? = sessionManager.fetchAccount()
        val bundle = intent.extras
        val profileTournamentId = bundle?.get("INTENT_ID")!!
        var TeamTournamentList: List<TeamTournamentResponse>? = null
        var idTeamTournament: Int? = 0
        var allMatches: List<MatchResponce>? = null

        apiClient.getApiService()
            .getTeamTournamentByTournament(
                profileTournamentId.toString()
            ).enqueue(object : Callback<List<TeamTournamentResponse>> {
                override fun onResponse(
                    call: Call<List<TeamTournamentResponse>>,
                    response: Response<List<TeamTournamentResponse>>
                ) {

                    TeamTournamentList = response.body()
                    println("Mae entre y tengo los equipos del torneo, vea la vara:")
                    println(TeamTournamentList)
                    println("")

                    apiClient.getApiService()
                        .getMatchesByTournament(
                            token = "Bearer ${sessionManager.fetchAuthToken()}",
                            Integer.parseInt(profileTournamentId.toString()),
                            "Scheduled"
                        ).enqueue(object : Callback<List<MatchResponce>> {
                            override fun onResponse(
                                call: Call<List<MatchResponce>>,
                                response: Response<List<MatchResponce>>
                            ) {
                                allMatches = response.body()!!
                            }

                            override fun onFailure(call: Call<List<MatchResponce>>, t: Throwable) {
                                println(call)
                                println(t)
                                println("error")
                                runOnUiThread() {
                                    Toast.makeText(
                                        applicationContext,
                                        "Error 3",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                        })



                    for (team in TeamTournamentList!!) {
                        if (team.idUser?.id == account!!.id!!) {
                            println("Mae entre y tengo el equipo que ocupo del torneo, vea:")
                            println(team.id)
                            println("")
                            idTeamTournament = team.id
                            break
                        }
                    }

                    for (matches in allMatches!!) {
                        if (matches.idTournament.id == profileTournamentId) {
                            if (matches.idTeamTournamentHome.id == idTeamTournament ||
                                matches.idTeamTournamentVisitor.id == idTeamTournament
                            ) {
                                apiClient.getApiService().deleteMatch(
                                    token = "Bearer ${sessionManager.fetchAuthToken()}",
                                    matches.id.toString()
                                ).enqueue(object : Callback<MatchResponce>{
                                    override fun onResponse(
                                        call: Call<MatchResponce>,
                                        response: Response<MatchResponce>
                                    ) {
                                        println("Borre borre")
                                    }

                                    override fun onFailure(
                                        call: Call<MatchResponce>,
                                        t: Throwable
                                    ) {
                                        println(call)
                                        println(t)
                                        println("error")
                                        runOnUiThread() {
                                            Toast.makeText(
                                                applicationContext,
                                                "Error 2",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    }

                                })
                            }
                        }
                    }

                    apiClient.getApiService().deleteTeamTournament(
                        token = "Bearer ${sessionManager.fetchAuthToken()}",
                        idTeamTournament.toString()
                    ).enqueue(object : Callback<TeamTournamentResponse> {
                        override fun onResponse(
                            call: Call<TeamTournamentResponse>,
                            response: Response<TeamTournamentResponse>
                        ) {
                            println(response.code())
                            if (response.code() == 200) {
                                LoadingScreen.hideLoading()
                                println("Mae elimine al grupo del torneo")
                                val intent =
                                    Intent(applicationContext, ProfileTournament::class.java)
                                intent.putExtra("INTENT_NAME", "$profileName")
                                intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                                intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                                intent.putExtra("INTENT_FORMAT", "$profileFormat")
                                intent.putExtra("INTENT_ID", "$profileId")
                                intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                                intent.putExtra("INTENT_MATCHES", "$profileMatches")
                                intent.putExtra("INTENT_ICON", "$profileIcon")
                                intent.putExtra("INTENT_STATUS", "$profileStatus")
                                startActivity(intent)
                                overridePendingTransition(0, 0);
                            }
                        }

                        override fun onFailure(
                            call: Call<TeamTournamentResponse>,
                            t: Throwable
                        ) {
                            println(call)
                            println(t)
                            println("error")
                            runOnUiThread() {
                                Toast.makeText(
                                    applicationContext,
                                    "Error 2",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    })

                }

                override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                    println(call)
                    println(t)
                    println("error")
                    runOnUiThread() {
                        Toast.makeText(
                            applicationContext,
                            "Error 1",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

            })

    }

//    private fun joinToTournament() {
//
//        //POST A DB A USERSTATS EN TEAM TOURNAMENT
//
//        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
//        val bundle = intent.extras
//        val profileId = bundle?.get("INTENT_ID")!!
//        val id : Int = Integer.parseInt("$profileId")
//        val account: AccountResponce? = sessionManager.fetchAccount()
//        val teamTournamentRequest = TeamTournamentRequest(goalsDone = 0, goalsReceived = 0, points = 0, TournamentResponse(id), UserResponse(account!!.id!!))
//
//        apiClient.getApiService().postTeamTournament(token = "Bearer ${sessionManager.fetchAuthToken()}",teamTournamentRequest).enqueue(object: Callback<TeamTournamentResponse>
//        {
//            override fun onResponse(call: Call<TeamTournamentResponse>, response: Response<TeamTournamentResponse>) {
//                LoadingScreen.hideLoading()
//                getTeamTournaments()
//            }
//
//            override fun onFailure(call: Call<TeamTournamentResponse>, t: Throwable) {
//                println(call)
//                println(t)
//                println("error")
//                runOnUiThread() {
//                    Toast.makeText(
//                        applicationContext,
//                        "Error al enviar la informacion, porfavor reintente.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                }
//            }
//        }
//        )
//
//    }

    private fun joinToTournament() {

        //POST A DB A USERSTATS EN TEAM TOURNAMENT

        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")!!
        val id: Int = Integer.parseInt("$profileId")
        val account: AccountResponce? = sessionManager.fetchAccount()
        val teamTournamentRequest = TeamTournamentRequest(
            goalsDone = 0,
            goalsReceived = 0,
            points = 0,
            TournamentResponse(id),
            UserResponse(account!!.id!!)
        )

        apiClient.getApiService().postTeamTournament(
            token = "Bearer ${sessionManager.fetchAuthToken()}",
            teamTournamentRequest
        ).enqueue(object : Callback<TeamTournamentResponse> {
            override fun onResponse(
                call: Call<TeamTournamentResponse>,
                response: Response<TeamTournamentResponse>
            ) {
                changeTournamentProfileInfo()
                LoadingScreen.hideLoading()
                val intent = Intent(applicationContext, ProfileTournament::class.java)
                intent.putExtra("INTENT_NAME", "$profileName")
                intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                intent.putExtra("INTENT_FORMAT", "$profileFormat")
                intent.putExtra("INTENT_ID", "$profileId")
                intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                intent.putExtra("INTENT_MATCHES", "$profileMatches")
                intent.putExtra("INTENT_ICON", "$profileIcon")
                intent.putExtra("INTENT_STATUS", "$profileStatus")
                startActivity(intent)
                overridePendingTransition(0, 0);
            }

            override fun onFailure(call: Call<TeamTournamentResponse>, t: Throwable) {
                println(call)
                println(t)
                println("error")
                runOnUiThread() {
                    Toast.makeText(
                        applicationContext,
                        "Error al enviar la informacion, porfavor reintente.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        )

    }

    private lateinit var tournamentProfileTeamAdapter: TournamentProfileTeamAdapter
    private var userStatsList = mutableListOf<UserStatsResponse>()
    private var tournamentProfileList = mutableListOf<UserStatsResponse>()
    private var userIdQuery: String = ""

    private fun getUserStats() {
        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService()
            .getListUserStatsByUsersId(token = "Bearer ${barrear}", userIdQuery)
            .enqueue(object : Callback<List<UserStatsResponse>> {
                override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                    System.out.println("error user stats")
                    HandleTeamTournamentError()
                }

                override fun onResponse(
                    call: Call<List<UserStatsResponse>>,
                    response: Response<List<UserStatsResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        System.out.println("success users stats")
                        val userStats: List<UserStatsResponse> = response.body()!!
                        userStatsList = userStats as MutableList<UserStatsResponse>

                        tournamentProfileList.clear()
                        tournamentProfileTeamAdapter.notifyDataSetChanged()

                        for (i: Int in 0..userStatsList.size - 1) {
                            tournamentProfileList.add(userStatsList[i])
                        }
                        tournamentProfileList.sortBy { it.nickName }

                        profileButtonStatusOptions()
                    }

                }
            })
    }

    private fun getTeamTournaments() {
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")!!
        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        apiClient.getApiService().getTeamTournamentByTournament("$profileId")
            .enqueue(object : Callback<List<TeamTournamentResponse>> {
                override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                    System.out.println("error team tournaments")
                    HandleTeamTournamentError()
                    LoadingScreen.hideLoading()
                }

                override fun onResponse(
                    call: Call<List<TeamTournamentResponse>>,
                    response: Response<List<TeamTournamentResponse>>
                ) {
                    LoadingScreen.hideLoading()
                    if (response.isSuccessful && response.body() != null) {
                        val teamTournaments: List<TeamTournamentResponse> = response.body()!!
                        teamTournamentList = teamTournaments as MutableList<TeamTournamentResponse>

                        tournamentProfileTeamList.clear()
                        tournamentProfileTeamAdapter.notifyDataSetChanged()

                        for (i: Int in 0..teamTournamentList.size - 1) {
                            tournamentProfileTeamList.add(teamTournamentList[i])
                            userIdQuery =
                                userIdQuery + teamTournamentList[i].idUser!!.id.toString() + ","

                        }
                        getUserStats()
                        changeTournamentProfileInfo()
                    }
                }
            })
    }

    fun HandleTeamTournamentError() {
        runOnUiThread() {
            Toast.makeText(applicationContext, "Error al cargar equipos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_profile_back)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            //onBackPressed()
        }
    }

    private fun bottomNavigationMenu() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_tournament)
        bottomNavigationView.setSelectedItemId(R.id.tournament_profile)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tournament_profile -> {
                    //current item
                }
                R.id.tournament_matches -> {
                    val intent = Intent(applicationContext, TournamentNextMatches::class.java)
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_NAME", "$profileName")
                    intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                    intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                    intent.putExtra("INTENT_FORMAT", "$profileFormat")
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                    intent.putExtra("INTENT_MATCHES", "$profileMatches")
                    intent.putExtra("INTENT_ICON", "$profileIcon")
                    intent.putExtra("INTENT_STATUS", "$profileStatus")
                    intent.putExtra("MATCH_STATUS", "Scheduled")
                    startActivity(intent)

                }
                R.id.tournament_results -> {

                    val intent = Intent(applicationContext, TournamentNextMatches::class.java)
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_NAME", "$profileName")
                    intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                    intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                    intent.putExtra("INTENT_FORMAT", "$profileFormat")
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                    intent.putExtra("INTENT_MATCHES", "$profileMatches")
                    intent.putExtra("INTENT_ICON", "$profileIcon")
                    intent.putExtra("INTENT_STATUS", "$profileStatus")
                    intent.putExtra("MATCH_STATUS", "Complete")
                    startActivity(intent)

                }
                R.id.tournament_table -> {
                    val intent = Intent(applicationContext, ProfileTournamentTable::class.java)
                    intent.putExtra("INTENT_NAME", "$profileName")
                    intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                    intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                    intent.putExtra("INTENT_FORMAT", "$profileFormat")
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                    intent.putExtra("INTENT_MATCHES", "$profileMatches")
                    intent.putExtra("INTENT_ICON", "$profileIcon")
                    intent.putExtra("INTENT_STATUS", "$profileStatus")
                    startActivity(intent)
                }
            }
            true

        }
    }

}