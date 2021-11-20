package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentProfileTeamAdapter
import com.mobile.heroes.mytournament.tournamentprofile.TournamentTableAdapter
import kotlinx.android.synthetic.main.tournament_table_body_center.*
import kotlin.random.Random
import kotlinx.android.synthetic.main.activity_profile_tournament.*
import kotlinx.android.synthetic.main.tournament_profile_body.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileTournamentTable : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var bottomNavigationView : BottomNavigationView

    private var teamNameList = mutableListOf<String>()
    private var teamPositionList = mutableListOf<String>()
    private var teamPointsList = mutableListOf<Int>()
    private var tournamentTeamsList = mutableListOf<TeamTournamentResponse>(
        TeamTournamentResponse(id = 1),
        TeamTournamentResponse(id = 2),
        TeamTournamentResponse(id = 3),
        TeamTournamentResponse(id = 4),
        TeamTournamentResponse(id = 5),
        TeamTournamentResponse(id = 6),
        TeamTournamentResponse(id = 7),
        TeamTournamentResponse(id = 8)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_table)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        backbutton()
        changeInfo()
        getTeamTournaments()
        getUserStats()


        tournamentTableAdapter = TournamentTableAdapter(teamPositionList, tournamentProfileList, tournamentTeamsList )

        rv_tournament_table.layoutManager = LinearLayoutManager(this)
        rv_tournament_table.adapter = tournamentTableAdapter

        //tournamentTableAdapter = TournamentTableAdapter(tournamentProfileList, teamPositionList,teamNameList, tournamentTeamsList)
        //rv_tournament_profile_teams.layoutManager = LinearLayoutManager(this)
        //rv_tournament_profile_teams.adapter = tournamentTableAdapter
    }

    fun changeInfo(){

        val bundle = intent.extras
        val profileName = bundle?.get("INTENT_NAME")
        val profileIcon = bundle?.get("INTENT_ICON")
        val profileDescription = bundle?.get("INTENT_DESCRIPTION")
        val profileStartDate = bundle?.get("INTENT_START_DATE")
        val profileFormat = bundle?.get("INTENT_FORMAT")
        val profileId = bundle?.get("INTENT_ID")
        val profileParticipants = bundle?.get("INTENT_PARTICIPANTS")
        val profileMatches = bundle?.get("INTENT_MATCHES")

        var profileTableTitleTextView : TextView = findViewById(R.id.tv_tournament_profile_table_name)
        profileTableTitleTextView.setText("$profileName")

        val imageBytes = Base64.decode("$profileIcon",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_table_head_image)
        profileIconImageView.setImageBitmap(image)

        bottomNavigationView = findViewById(R.id.bottom_navigation_tournament)
        bottomNavigationView.setSelectedItemId(R.id.tournament_table)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when (it.itemId){
                R.id.tournament_profile -> {
                    val intent = Intent(applicationContext, ProfileTournament::class.java)
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
                R.id.tournament_table ->{ }
            }
            true

        }
    }


    private lateinit var tournamentTableAdapter: TournamentTableAdapter
    private var userStatsList = mutableListOf<UserStatsResponse>()
    private var tournamentProfileList = mutableListOf<UserStatsResponse>()

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

                        System.out.println("success user stats table")
                        val userStats : List<UserStatsResponse> = response.body()!!
                        userStatsList = userStats as MutableList<UserStatsResponse>

                        tournamentProfileList.clear()
                        tournamentTableAdapter.notifyDataSetChanged()

                        for(i:Int in 0..userStatsList.size-1){
                            tournamentProfileList.add(userStatsList[i])
                            //System.out.println("add user")
                            //System.out.println(userStatsList[i])

                        }

                        for(i:Int in 0..userStatsList.size-1){
                            val pointsRandomValue = Random.nextInt(0,30)
                            tournamentTeamsList[i].points = pointsRandomValue
                        }
                        postpositionsToTableList()
                        orderListByPoints()
                    }

                }
            })
    }

    private lateinit var tournamentProfileTeamAdapter: TournamentProfileTeamAdapter
    private var tournamentTableList = mutableListOf<TeamTournamentResponse>()
    private var teamTournamentList = mutableListOf<TeamTournamentResponse>()
    private var tournamentProfileTeamList = mutableListOf<TeamTournamentResponse>()

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

                        tournamentTableList.clear()
                        tournamentTableAdapter.notifyDataSetChanged()

                        for(i:Int in 0..teamTournamentList.size-1){

                            tournamentTableList.add(teamTournamentList[i])
                            System.out.println(teamTournamentList[i])

                        }
                    }
                }
            })
    }

    private fun addToList(name:String, points:Int){

        teamPointsList.add(points)
    }

    private fun postpositionsToTableList(){
        for(i:Int in 1..tournamentTeamsList.size){
            teamPositionList.add(i.toString())
        }
    }

    private fun orderListByPoints(){
       // System.out.println(tournamentTeamsList)

        tournamentTeamsList.sortByDescending{it.points}

        //System.out.println(tournamentTeamsList)

    }

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_table_back)
        backButton.setOnClickListener {
            //startActivity(Intent(this,ProfileTournament::class.java))
            onBackPressed()
        }
    }
}