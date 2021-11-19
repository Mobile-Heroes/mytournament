package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.tournamentprofile.TournamentTableAdapter
import kotlinx.android.synthetic.main.tournament_table_body_center.*
import kotlin.random.Random

class ProfileTournamentTable : AppCompatActivity() {

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

        changeInfo()
        postToTableList()

        rv_tournament_table.layoutManager = LinearLayoutManager(this)
        rv_tournament_table.adapter = TournamentTableAdapter(teamPositionList,teamNameList, tournamentTeamsList )
    }

    fun changeInfo(){

        val bundle = intent.extras
        val profileTableTitle = bundle?.get("INTENT_TABLE_TITLE")

        var profileTableTitleTextView : TextView = findViewById(R.id.tv_tournament_profile_table_name)
        profileTableTitleTextView.setText("$profileTableTitle")

    }

    private fun addToList(name:String, points:Int){

        teamPointsList.add(points)
    }

    private fun postToTableList(){
        for(i:Int in 0..tournamentTeamsList.size-1){
            val pointsRandomValue = Random.nextInt(0,30)
            teamNameList.add("Equipo ${i+1}")
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
        if(tournamentTeamsList!=null){
            tournamentTeamsList.sortedBy { it.points }
        }
    }


}