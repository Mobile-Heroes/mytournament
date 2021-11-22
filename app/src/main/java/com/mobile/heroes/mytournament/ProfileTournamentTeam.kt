package com.mobile.heroes.mytournament

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class ProfileTournamentTeam : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_team)
        backbutton()

        changeTeamProfileInfo()

    }

    fun changeTeamProfileInfo(){
        val bundle = intent.extras
        val profileNickName = bundle?.get("INTENT_NICK_NAME")
        val profileTournaments = bundle?.get("INTENT_TOURNAMENTS")
        val profileTitles = bundle?.get("INTENT_TITLES")
        val profileGoals = bundle?.get("INTENT_GOALS")
        val profilePicture = bundle?.get("INTENT_TEAM_PICTURE")
        val profileIdUser = bundle?.get("INTENT_ID_USER")
        
        var profileNameTextView : TextView = findViewById(R.id.tv_tournament_team_profile_name)
        profileNameTextView.setText("$profileNickName")

        var profilePointsTextView : TextView = findViewById(R.id.tv_tournament_team_profile_tournaments_value)
        profilePointsTextView.setText("$profileTournaments")

        var profileGoalsDoneTextView : TextView = findViewById(R.id.tv_tournament_team_profile_goals_value)
        profileGoalsDoneTextView.setText("$profileGoals")

        val imageBytes = Base64.decode("$profilePicture",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_team_profile_head_image)
        profileIconImageView.setImageBitmap(image)

    }

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_team_back)
        backButton.setOnClickListener {
            //startActivity(Intent(this,ProfileTournament::class.java))
            onBackPressed()
        }
    }

}
