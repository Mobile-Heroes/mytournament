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
import com.mobile.heroes.mytournament.networking.services.GroupResource.GroupResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.tournamentprofile.TeamInGroupDTO
import com.mobile.heroes.mytournament.tournamentprofile.TournamentGroupAdapter
import com.mobile.heroes.mytournament.tournamentprofile.TournamentTableAdapter
import kotlinx.android.synthetic.main.tournament_group_body.*
import kotlinx.android.synthetic.main.tournament_table_body_center.*
import kotlinx.android.synthetic.main.tournament_table_body_center.rv_tournament_table
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileTournamentGroup : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var bottomNavigationView : BottomNavigationView

    private var profileName: Any? = ""
    private var profileIcon: Any? = ""
    private var profileDescription: Any? = ""
    private var profileStartDate: Any? = ""
    private var profileFormat: Any? = ""
    private var profileId: Any? = ""
    private var profileParticipants: Any? = ""
    private var profileMatches: Any? = ""
    private var profileStatus: Any? = ""

    private lateinit var tournamentGroupAdapter: TournamentGroupAdapter
    private var teamTournamentGeneralTableList = mutableListOf<TeamTournamentResponse>()

    private var tournamentTableList = mutableListOf<TeamTournamentResponse>()
    private var tournamentTableListDTO1 = mutableListOf<TeamTournamentResponse>()
    private var tournamentTableListDTO2 = mutableListOf<TeamTournamentResponse>()

    private var groupsWithTeamsList = mutableListOf<TeamInGroupDTO>()

    private var userStatsList = mutableListOf<UserStatsResponse>()
    private var tournamentProfileList = mutableListOf<UserStatsResponse>()
    private var tournamentProfileListDTO = mutableListOf<UserStatsResponse>()

    private var groupList = mutableListOf<GroupResponse>()
    private var userIdQuery : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_group)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        loadIntentExtras()
        backbutton()
        changeInfo()
        bottomNavigationMenu()

        displayTournamentTableByType()
    }

    private fun displayTournamentTableByType() {
        var tournamentTableTitleTextView : TextView = findViewById(R.id.tv_tournament_group_body_title)
        tournamentTableTitleTextView.setText("Tablas de grupos")

        getTeamsInGroups()

        //datosQuemadosDTO()

        tournamentGroupAdapter = TournamentGroupAdapter(groupsWithTeamsList, tournamentProfileListDTO)

        rv_tournament_group.layoutManager = LinearLayoutManager(this)
        rv_tournament_group.adapter = tournamentGroupAdapter
    }

    private fun datosQuemadosDTO(){

        groupsWithTeamsList.clear()

        //GRUPO 1
        tournamentTableListDTO1.clear()

        for(i:Int in 1..4){

            for(j:Int in 0..tournamentTableList.size-1){

            /*var team = TeamTournamentResponse(i)
            team.goalsDone = i
            team.goalsReceived = i-1
            team.points = i
            team.countMatches = i*/

            var team1 = tournamentTableList[j]

            tournamentTableListDTO1.add(team1)
            }
        }

        var grupo1 = GroupResponse(1)
        grupo1.name = "Group A"
        grupo1.grade = 0
        grupo1.type = "Groups"

        var teamInGroupDTO1 = TeamInGroupDTO(grupo1)
        teamInGroupDTO1.teamTournamentDTOList = tournamentTableListDTO1

        groupsWithTeamsList.add(teamInGroupDTO1)

        //GRUPO 2

        if(tournamentTableList.size == 8){
            tournamentTableListDTO2.clear()

            for(i:Int in 1..4){
                for(j:Int in 0..tournamentTableList.size-1){
                    /*var team2 = TeamTournamentResponse(i)
                    team2.goalsDone = i
                    team2.goalsReceived = i+1
                    team2.points = i
                    team2.countMatches = i*/

                    var team2 = tournamentTableList[j]

                    tournamentTableListDTO2.add(team2)
                }
            }

            var grupo2 = GroupResponse(2)
            grupo2.name = "Group B"
            grupo2.grade = 0
            grupo2.type = "Groups"

            var teamInGroupDTO2 = TeamInGroupDTO(grupo2)
            teamInGroupDTO2.teamTournamentDTOList = tournamentTableListDTO2

            groupsWithTeamsList.add(teamInGroupDTO2)
        }



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

        var profileTableTitleTextView : TextView = findViewById(R.id.tv_tournament_profile_group_name)
        profileTableTitleTextView.setText("$profileName")

        val imageBytes = Base64.decode("$profileIcon",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_group_head_image)
        profileIconImageView.setImageBitmap(image)
    }

    private fun getUserStats() {

        apiClient.getApiService().getListUserStatsByUsersId(userIdQuery)
            .enqueue(object : Callback<List<UserStatsResponse>> {
                override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                    System.out.println("error user stats")
                }

                override fun onResponse(
                    call: Call<List<UserStatsResponse>>,
                    response: Response<List<UserStatsResponse>>
                ) {
                    if(response.isSuccessful && response.body() != null){

                        System.out.println("success group user stats")
                        val userStats : List<UserStatsResponse> = response.body()!!
                        userStatsList = userStats as MutableList<UserStatsResponse>

                        tournamentProfileList.clear()
                        tournamentGroupAdapter.notifyDataSetChanged()

                        for(i:Int in 0..userStatsList.size-1){
                            tournamentProfileList.add(userStatsList[i])
                        }

                    }
                }
            })
    }



    private fun getTeamsInGroups() {
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")

        LoadingScreen.displayLoadingWithText(this, " ", false)

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

                        tournamentTableList.clear()
                        tournamentGroupAdapter.notifyDataSetChanged()

                        for(i:Int in 0..teamTournamentGeneralTableList.size-1){
                            tournamentTableList.add(teamTournamentGeneralTableList[i])
                            userIdQuery = userIdQuery + teamTournamentGeneralTableList[i].idUser!!.id.toString() + ","
                        }
                        tournamentTableList.sortByDescending{it.points}
                        //getUserStats()
                        getUserStatsDTO()
                    }
                }
            })
    }

    private fun getUserStatsDTO() {

        apiClient.getApiService().getListUserStatsByUsersId(userIdQuery)
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
                        tournamentGroupAdapter.notifyDataSetChanged()

                        datosQuemadosDTO()

                        val cantidadEquipos = groupsWithTeamsList.size * 4

                        for(i:Int in 1..cantidadEquipos){

                            for(j:Int in 0..userStatsList.size-1){
                                var userStatsTest = UserStatsResponse(j)
                                userStatsTest.nickName = userStatsList[j].nickName
                                userStatsTest.icon = userStatsList[j].icon
                                tournamentProfileListDTO.add(userStatsTest)
                            }
                        }

                    }
                }
            })
    }

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_group_back)
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