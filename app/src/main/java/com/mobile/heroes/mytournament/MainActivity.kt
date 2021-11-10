package com.mobile.heroes.mytournament

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.heroes.mytournament.signup.SignUpOrganizer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up_organizer.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button2.setOnClickListener { view -> val activityIntent= Intent(this,SignUpOrganizer::class.java)
            startActivity(activityIntent)

        }

    }
}