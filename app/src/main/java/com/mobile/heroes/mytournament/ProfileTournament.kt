package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentProfileTeamAdapter
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
        profileId = bundle?.get("INTENT_ID")
        profileParticipants = bundle?.get("INTENT_PARTICIPANTS")
        profileMatches = bundle?.get("INTENT_MATCHES")
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

                    }

                }
            })
    }

    private fun getTeamTournaments() {
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")
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
                R.id.tournament_profile -> { }
                R.id.tournament_matches ->{ }
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
                    startActivity(intent)
                }
            }
            true

        }
    }

}