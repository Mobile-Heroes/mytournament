package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentTableAdapter
import kotlinx.android.synthetic.main.tournament_table_body_center.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileTournamentTable : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var bottomNavigationView : BottomNavigationView

    private var teamPositionList = mutableListOf<String>()

    private var profileName: Any? = ""
    private var profileIcon: Any? = ""
    private var profileDescription: Any? = ""
    private var profileStartDate: Any? = ""
    private var profileFormat: Any? = ""
    private var profileId: Any? = ""
    private var profileParticipants: Any? = ""
    private var profileMatches: Any? = ""
    private var profileStatus: Any? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_table)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        loadIntentExtras()
        backbutton()
        changeInfo()
        bottomNavigationMenu()

        displayTournamentTableByType()
    }

    private fun displayTournamentTableByType() {
        when (profileFormat) {
            "GeneralTable" -> displayGeneralTableFormat()
            "DirectDelete" -> displayDirectDeleteFormat()
            "Groups" -> displayGroupsFormat()
        }
    }

    private fun displayGeneralTableFormat() {
        removeGroupsTableBody()

        var tournamentTableTitleTextView : TextView = findViewById(R.id.tv_tournament_table_body_title)
        tournamentTableTitleTextView.setText("Tabla general de posiciones")

        getTeamTournamentsForGeneralTable()

        tournamentTableAdapter = TournamentTableAdapter(tournamentProfileList, tournamentGeneralTableList )

        rv_tournament_table.layoutManager = LinearLayoutManager(this)
        rv_tournament_table.adapter = tournamentTableAdapter
    }

    private fun displayDirectDeleteFormat() {
        removeGeneralTableBody()
        removeGroupsTableBody()

        var tournamentTableTitleTextView : TextView = findViewById(R.id.tv_tournament_table_body_title)
        tournamentTableTitleTextView.setText("Eliminación directa")

    }

    private fun displayGroupsFormat() {
        removeGeneralTableBody()

        var tournamentTableTitleTextView : TextView = findViewById(R.id.tv_tournament_table_body_title)
        tournamentTableTitleTextView.setText("Tablas de grupos")


    }

    private fun removeGeneralTableBody() {
        var generalTableView : View = findViewById(R.id.in_tournament_table_profile_body)
        generalTableView.setVisibility(View.GONE)
    }

    private fun removeGroupsTableBody() {
        var groupsTableView : View = findViewById(R.id.in_tournament_groups_body)
        groupsTableView.setVisibility(View.GONE)
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

    fun changeInfo(){

        val bundle = intent.extras

        var profileTableTitleTextView : TextView = findViewById(R.id.tv_tournament_profile_table_name)
        profileTableTitleTextView.setText("$profileName")

        val imageBytes = Base64.decode("$profileIcon",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_table_head_image)
        profileIconImageView.setImageBitmap(image)
    }

    private lateinit var tournamentTableAdapter: TournamentTableAdapter
    private var userStatsList = mutableListOf<UserStatsResponse>()
    private var tournamentProfileList = mutableListOf<UserStatsResponse>()
    private var userIdQuery : String = ""

    private fun getUserStats() {

        val barrear: String = sessionManager.fetchAuthToken()!!;

        apiClient.getApiService().getListUserStatsByUsersId(token = "Bearer ${barrear}", userIdQuery)
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
                        }

                    }
                }
            })
    }

    private var tournamentGeneralTableList = mutableListOf<TeamTournamentResponse>()
    private var teamTournamentGeneralTableList = mutableListOf<TeamTournamentResponse>()

    private fun getTeamTournamentsForGeneralTable() {

        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")

        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)

        apiClient.getApiService().getTeamTournamentByTournament("$profileId")
            .enqueue(object : Callback<List<TeamTournamentResponse>> {
                override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                    System.out.println("error team tournaments")
                    LoadingScreen.hideLoading()
                }
                override fun onResponse(
                    call: Call<List<TeamTournamentResponse>>,
                    response: Response<List<TeamTournamentResponse>>
                ) {
                    LoadingScreen.hideLoading()
                    if(response.isSuccessful && response.body() != null){

                        val teamTournaments : List<TeamTournamentResponse> = response.body()!!
                        teamTournamentGeneralTableList = teamTournaments as MutableList<TeamTournamentResponse>

                        tournamentGeneralTableList.clear()
                        tournamentTableAdapter.notifyDataSetChanged()

                        for(i:Int in 0..teamTournamentGeneralTableList.size-1){
                            tournamentGeneralTableList.add(teamTournamentGeneralTableList[i])
                            userIdQuery = userIdQuery + teamTournamentGeneralTableList[i].idUser!!.id.toString() + ","
                        }
                        tournamentGeneralTableList.sortByDescending{it.points}
                        getUserStats()
                    }
                }
            })
    }





    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_table_back)
        backButton.setOnClickListener {
            //startActivity(Intent(this,ProfileTournament::class.java))
            onBackPressed()
        }
    }

    private fun bottomNavigationMenu() {
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
                    intent.putExtra("INTENT_STATUS", "$profileStatus")
                    startActivity(intent)
                }
                R.id.tournament_matches ->{
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
                R.id.tournament_results ->{
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
                R.id.tournament_table ->{
                    //current item
                }
            }
            true

        }
    }
}