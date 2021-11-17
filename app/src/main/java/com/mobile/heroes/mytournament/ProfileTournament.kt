package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class ProfileTournament : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament)

        changeProfileInfo()
    }

    fun changeProfileInfo(){
        val bundle = intent.extras
        val profileName = bundle?.get("INTENT_NAME")
        val profileDescription = bundle?.get("INTENT_DESCRIPTION")
        val profileStartDate = bundle?.get("INTENT_START_DATE")
        val profileFormat = bundle?.get("INTENT_FORMAT")
        val profileId = bundle?.get("INTENT_ID")
        val profileParticipants = bundle?.get("INTENT_PARTICIPANTS")
        val profileMatches = bundle?.get("INTENT_MATCHES")
        val profileIcon = bundle?.get("INTENT_ICON")


        var profileNameTextView : TextView = findViewById(R.id.tv_tournament_profile_name)
        profileNameTextView.setText("$profileName")

        var profileDescriptionTextView : TextView = findViewById(R.id.tv_tournament_profile_description)
        profileDescriptionTextView.setText("$profileDescription")

        var profileStartDateTextView : TextView = findViewById(R.id.tv_tournament_profile_date_value)
        profileStartDateTextView.setText("$profileStartDate")

        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_head_image)
        profileIconImageView.setImageResource(R.drawable.liga_icon)

    }
}