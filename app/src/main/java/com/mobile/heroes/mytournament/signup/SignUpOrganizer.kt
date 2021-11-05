package com.mobile.heroes.mytournament.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.heroes.mytournament.MainActivity
import com.mobile.heroes.mytournament.R
import kotlinx.android.synthetic.main.activity_sign_up_organizer.*

class SignUpOrganizer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_organizer)

        txtviewLogin.setOnClickListener{view ->val activityIntent= Intent(this, MainActivity::class.java)
            startActivity(activityIntent)
        }

    }
}