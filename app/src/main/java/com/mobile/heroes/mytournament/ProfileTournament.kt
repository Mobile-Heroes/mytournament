package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileTournament : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament)

        changeProfileInfo()
    }

    fun changeProfileInfo(){
        var titleTextView : TextView
        val bundle = intent.extras
        val name = bundle?.get("INTENT_NAME")

        titleTextView  = findViewById(R.id.tv_profile_tournament_title)
        titleTextView.setText("$name")
    }
}