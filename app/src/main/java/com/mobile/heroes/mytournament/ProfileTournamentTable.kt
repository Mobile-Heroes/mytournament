package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileTournamentTable : AppCompatActivity() {

    private var teamNameList = mutableListOf<String>()
    private var teamImageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_table)

        changeInfo()






    }

    fun changeInfo(){

        val bundle = intent.extras
        val profileTableTitle = bundle?.get("INTENT_TABLE_TITLE")

        var profileTableTitleTextView : TextView = findViewById(R.id.tv_tournament_profile_table_name)
        profileTableTitleTextView.setText("$profileTableTitle")

    }

    private fun addToList(name:String, image:Int){
        teamImageList.add(image)
        teamNameList.add(name)
    }

    private fun postToTeamList(){
        for(i:Int in 1..8){
            addToList("Equipo $i",  R.drawable.ic_form_tournament_name)
        }
    }

}