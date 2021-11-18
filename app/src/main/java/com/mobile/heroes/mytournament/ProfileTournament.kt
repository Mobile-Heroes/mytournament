package com.mobile.heroes.mytournament

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.tournamentprofile.TournamentProfileTeamAdapter
import kotlinx.android.synthetic.main.activity_tournament.*
import kotlinx.android.synthetic.main.tournament_profile_body.*

class ProfileTournament : AppCompatActivity() {

    private var teamNameList = mutableListOf<String>()
    private var teamImageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament)

        changeProfileInfo()
        postToTeamList()

        rv_tournament_profile_teams.layoutManager = LinearLayoutManager(this)
        rv_tournament_profile_teams.adapter = TournamentProfileTeamAdapter(teamNameList,teamImageList)
    }

    fun changeProfileInfo(){
        val bundle = intent.extras
        val profileName = bundle?.get("INTENT_NAME")
        var profileIcon = bundle?.get("INTENT_ICON")
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

        val imageBytes = Base64.decode("$profileIcon",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_head_image)
        profileIconImageView.setImageBitmap(image)

    }


    private fun addToList(name:String, image:Int){
        teamNameList.add(name)
        teamImageList.add(image)
    }

    private fun postToTeamList(){
        for(i:Int in 1..8){
            addToList("Equipo $i",  R.drawable.ic_form_tournament_name)
        }
    }
}