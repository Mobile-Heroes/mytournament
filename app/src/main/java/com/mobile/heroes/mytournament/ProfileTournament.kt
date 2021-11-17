package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileTournament : AppCompatActivity() {

    private lateinit var titleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament)

        titleTextView  = findViewById(R.id.tv_profile_tournament_title)

        titleTextView.setText("Cambio")
    }
}