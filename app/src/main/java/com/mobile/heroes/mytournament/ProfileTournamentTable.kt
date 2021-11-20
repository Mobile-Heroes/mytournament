package com.mobile.heroes.mytournament

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentTableAdapter
import kotlinx.android.synthetic.main.tournament_table_body_center.*
import kotlin.random.Random

class ProfileTournamentTable : AppCompatActivity() {

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

        backbutton()
        changeInfo()
        postToTableList()

        rv_tournament_table.layoutManager = LinearLayoutManager(this)
        rv_tournament_table.adapter = TournamentTableAdapter(teamPositionList,teamNameList, tournamentTeamsList )
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

    private fun addToList(name:String, points:Int){

        teamPointsList.add(points)
    }

    private fun postToTableList(){
        for(i:Int in 0..tournamentTeamsList.size-1){
            val pointsRandomValue = Random.nextInt(0,30)
            teamNameList.add("Equipo ${i+1}")
            tournamentTeamsList[i].idUser = i
            tournamentTeamsList[i].points = pointsRandomValue
        }
        postpositionsToTableList()
        orderListByPoints()
    }

    private fun postpositionsToTableList(){
        for(i:Int in 1..tournamentTeamsList.size){
            teamPositionList.add(i.toString())
        }
    }

    private fun orderListByPoints(){
        System.out.println(tournamentTeamsList)

        tournamentTeamsList.sortByDescending{it.points}

        System.out.println(tournamentTeamsList)

    }

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_table_back)
        backButton.setOnClickListener {
            //startActivity(Intent(this,ProfileTournament::class.java))
            onBackPressed()
        }
    }
}