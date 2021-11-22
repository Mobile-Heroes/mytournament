package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentRequest
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentProfileTeamAdapter
import kotlinx.android.synthetic.main.activity_create_tournament.*
import kotlinx.android.synthetic.main.tournament_profile_body.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileTournament : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    private var teamTournamentList = mutableListOf<TeamTournamentResponse>()
    private var tournamentProfileTeamList = mutableListOf<TeamTournamentResponse>()

    private lateinit var bottomNavigationView : BottomNavigationView
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
        changeTournamentProfileInfo()

        getTeamTournaments()

        tournamentProfileTeamAdapter = TournamentProfileTeamAdapter(tournamentProfileList)
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
        profileId = bundle!!.get("INTENT_ID")
        profileParticipants = bundle?.get("INTENT_PARTICIPANTS")
        profileMatches = bundle?.get("INTENT_MATCHES")
        profileStatus = bundle?.get("INTENT_STATUS")
    }

    fun changeTournamentProfileInfo(){
        var profileNameTextView : TextView = findViewById(R.id.tv_tournament_profile_name)
        profileNameTextView.setText("$profileName")

        var profileDescriptionTextView : TextView = findViewById(R.id.tv_tournament_profile_description)
        profileDescriptionTextView.setText("$profileDescription")

        var profileStartDateTextView : TextView = findViewById(R.id.tv_tournament_profile_date_value)
        profileStartDateTextView.setText("$profileStartDate")

        var profileParticipantsTextView : TextView = findViewById(R.id.tv_tournament_profile_participants_value)
        profileParticipantsTextView .setText("$profileParticipants")

        val imageBytes = Base64.decode("$profileIcon",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_head_image)
        profileIconImageView.setImageBitmap(image)
    }

    private fun profileButtonStatusOptions() {
        if(profileStatus == "Active"){
            favoriteButtonActions()
        }
        if(profileStatus == "InProgress"){
            joinTournamentButtonActions()
        }
    }

    private fun favoriteButtonActions() {
        var profileActionButton : Button = findViewById(R.id.bt_tournament_profile_action)
        profileActionButton.setText("Favorito")
        profileActionButton.setBackgroundColor(resources.getColor(R.color.verde))
        profileActionButton.setTextColor(resources.getColor(R.color.white))

        profileActionButton.setOnClickListener {

            val buttonText = profileActionButton.getText().toString()

            if(buttonText == "Favorito"){
                profileActionButton.setText("Siguiendo")
                profileActionButton.setBackgroundColor(resources.getColor(R.color.gris))
                profileActionButton.setTextColor(resources.getColor(R.color.black))
            }

            if(buttonText == "Siguiendo"){
                profileActionButton.setText("Favorito")
                profileActionButton.setBackgroundColor(resources.getColor(R.color.verde))
                profileActionButton.setTextColor(resources.getColor(R.color.white))

            }

        }

    }

    private fun joinTournamentButtonActions() {
        var profileActionButton : Button = findViewById(R.id.bt_tournament_profile_action)

        val userStats = sessionManager.fetchUserStats()
        val userId = userStats?.id

        for(i:Int in 0..tournamentProfileList.size-1){
            if(tournamentProfileList[i].id == userId){
                checkIfJoinedAleady = true
                profileActionButton.setText("Suscrito")
                profileActionButton.setBackgroundColor(resources.getColor(R.color.gris))
                profileActionButton.setTextColor(resources.getColor(R.color.black))
            }
        }
        if(checkIfJoinedAleady == false){

            profileActionButton.setText("Unirse")
            profileActionButton.setBackgroundColor(resources.getColor(R.color.verde))
            profileActionButton.setTextColor(resources.getColor(R.color.white))

            profileActionButton.setOnClickListener {
                joinToTournament()
                /*profileActionButton.setText("Suscrito")
                profileActionButton.setBackgroundColor(resources.getColor(R.color.gris))
                profileActionButton.setTextColor(resources.getColor(R.color.black))
                checkIfJoinedAleady = true*/
            }
        }
    }

    private fun joinToTournament() {

        //POST A DB A USERSTATS EN TEAM TOURNAMENT

        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")!!
        val id : Int = Integer.parseInt("$profileId")
        val account: AccountResponce? = sessionManager.fetchAccount()
        val teamTournamentRequest = TeamTournamentRequest(goalsDone = 0, goalsReceived = 0, points = 0, TournamentResponse(id), UserResponse(account!!.id!!))

        apiClient.getApiService().postTeamTournament(token = "Bearer ${sessionManager.fetchAuthToken()}",teamTournamentRequest).enqueue(object: Callback<TeamTournamentResponse>
        {
            override fun onResponse(call: Call<TeamTournamentResponse>, response: Response<TeamTournamentResponse>) {
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
    private var userIdQuery : String = ""

    private fun getUserStats() {
        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getListUserStatsByUsersId(token = "Bearer ${barrear}", userIdQuery)
            .enqueue(object : Callback<List<UserStatsResponse>> {
                override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                    System.out.println("error user stats")
                    HandleTeamTournamentError()
                }

                override fun onResponse(
                    call: Call<List<UserStatsResponse>>,
                    response: Response<List<UserStatsResponse>>
                ) {
                    if(response.isSuccessful && response.body() != null){

                        System.out.println("success users stats")
                        val userStats : List<UserStatsResponse> = response.body()!!
                        userStatsList = userStats as MutableList<UserStatsResponse>

                        tournamentProfileList.clear()
                        tournamentProfileTeamAdapter.notifyDataSetChanged()

                        for(i:Int in 0..userStatsList.size-1){
                            tournamentProfileList.add(userStatsList[i])
                        }
                        tournamentProfileList.sortBy{it.nickName}

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
                    if(response.isSuccessful && response.body() != null){
                        val teamTournaments : List<TeamTournamentResponse> = response.body()!!
                        teamTournamentList = teamTournaments as MutableList<TeamTournamentResponse>

                        tournamentProfileTeamList.clear()
                        tournamentProfileTeamAdapter.notifyDataSetChanged()

                        for(i:Int in 0..teamTournamentList.size-1){
                            tournamentProfileTeamList.add(teamTournamentList[i])
                            userIdQuery = userIdQuery + teamTournamentList[i].idUser!!.id.toString() + ","

                        }
                        getUserStats()

                    }
                }
            })
    }

    fun HandleTeamTournamentError() {
        runOnUiThread(){
            Toast.makeText(applicationContext, "Error al cargar equipos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_profile_back)
        backButton.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            //onBackPressed()
        }
    }

    private fun bottomNavigationMenu() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_tournament)
        bottomNavigationView.setSelectedItemId(R.id.tournament_profile)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when (it.itemId){
                R.id.tournament_profile -> {
                    //current item
                }
                R.id.tournament_matches ->{

                }
                R.id.tournament_results ->{ }
                R.id.tournament_table ->{
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