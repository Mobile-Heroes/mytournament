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
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentProfileTeamAdapter
import kotlinx.android.synthetic.main.activity_profile_tournament.*
import kotlinx.android.synthetic.main.fragment_feed_destination.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        backbutton()
        changeTournamentProfileInfo()

        getTeamTournaments()
        getUserStats()

        //rv_tournament_profile_teams.layoutManager = LinearLayoutManager(this)
        //rv_tournament_profile_teams.adapter = TournamentProfileTeamAdapter(teamNameList,teamImageList)

        tournamentProfileTeamAdapter = TournamentProfileTeamAdapter(tournamentProfileList)
        rv_tournament_profile_teams.layoutManager = LinearLayoutManager(this)
        rv_tournament_profile_teams.adapter = tournamentProfileTeamAdapter

    }

    fun changeTournamentProfileInfo(){

        val bundle = intent.extras
        val profileName = bundle?.get("INTENT_NAME")
        val profileIcon = bundle?.get("INTENT_ICON")
        val profileDescription = bundle?.get("INTENT_DESCRIPTION")
        val profileStartDate = bundle?.get("INTENT_START_DATE")
        val profileFormat = bundle?.get("INTENT_FORMAT")
        val profileId = bundle?.get("INTENT_ID")
        val profileParticipants = bundle?.get("INTENT_PARTICIPANTS")
        val profileMatches = bundle?.get("INTENT_MATCHES")

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

        bottomNavigationView = findViewById(R.id.bottom_navigation_tournament)
        bottomNavigationView.setSelectedItemId(R.id.tournament_profile)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when (it.itemId){
                R.id.tournament_profile -> { }
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

    private lateinit var tournamentProfileTeamAdapter: TournamentProfileTeamAdapter
    private var userStatsList = mutableListOf<UserStatsResponse>()
    private var tournamentProfileList = mutableListOf<UserStatsResponse>()
    private var tournamentTeamsList = mutableListOf<TeamTournamentResponse>(
        TeamTournamentResponse(id = 2),
        TeamTournamentResponse(id = 3)
    )

    private fun getUserStats() {
        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getUserStatsInList(token = "Bearer ${barrear}")
            .enqueue(object : Callback<List<UserStatsResponse>> {
                override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                    System.out.println("error user stats")
                }

                override fun onResponse(
                    call: Call<List<UserStatsResponse>>,
                    response: Response<List<UserStatsResponse>>
                ) {
                    if(response.isSuccessful && response.body() != null){

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

        apiClient.getApiService().getTeamTournament()
            .enqueue(object : Callback<List<TeamTournamentResponse>> {
                override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                    System.out.println("error team tournaments")
                }

                override fun onResponse(
                    call: Call<List<TeamTournamentResponse>>,
                    response: Response<List<TeamTournamentResponse>>
                ) {
                    if(response.isSuccessful && response.body() != null){
                        System.out.println("success team tournament")



                        val teamTournaments : List<TeamTournamentResponse> = response.body()!!
                        teamTournamentList = teamTournaments as MutableList<TeamTournamentResponse>

                        tournamentProfileTeamList.clear()
                        tournamentProfileTeamAdapter.notifyDataSetChanged()

                        for(i:Int in 0..teamTournamentList.size-1){
                            System.out.println("add")
                            tournamentProfileTeamList.add(teamTournamentList[i])
                            System.out.println(teamTournamentList[i])

                        }
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

}