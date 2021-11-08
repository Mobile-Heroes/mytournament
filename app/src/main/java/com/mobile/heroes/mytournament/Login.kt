package com.mobile.heroes.mytournament

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.heroes.mytournament.signup.SignUpOrganizer
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtviewNewAccount.setOnClickListener{view ->val activityIntent= Intent(this, SignUpOrganizer::class.java)
            startActivity(activityIntent)}
    }
}