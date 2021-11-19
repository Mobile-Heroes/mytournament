package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.tournamentprofile.TournamentTableAdapter
import kotlinx.android.synthetic.main.tournament_table_body_center.*

class ProfileTournamentTable : AppCompatActivity() {

    private var teamNameList = mutableListOf<String>()
    private var teamPositionList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_table)

        changeInfo()
        postToTanbleList()

        rv_tournament_table.layoutManager = LinearLayoutManager(this)
        rv_tournament_table.adapter = TournamentTableAdapter(teamPositionList,teamNameList )
    }

    fun changeInfo(){

        val bundle = intent.extras
        val profileTableTitle = bundle?.get("INTENT_TABLE_TITLE")

        var profileTableTitleTextView : TextView = findViewById(R.id.tv_tournament_profile_table_name)
        profileTableTitleTextView.setText("$profileTableTitle")

    }

    private fun addToList(position:String, name:String){
        teamNameList.add(name)
        teamPositionList.add(position)
    }

    private fun postToTanbleList(){
        for(i:Int in 1..8){
            addToList(i.toString(), "Equipo $i")
        }
    }

}