package com.mobile.heroes.mytournament

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView

class ProfileTournamentTeam : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_team)

        changeTeamProfileInfo()
    }

    fun changeTeamProfileInfo(){
        val bundle = intent.extras
        val profileName = bundle?.get("INTENT_NAME")

        var profileNameTextView : TextView = findViewById(R.id.tv_tournament_team_profile_name)
        profileNameTextView.setText("$profileName")

    }

}