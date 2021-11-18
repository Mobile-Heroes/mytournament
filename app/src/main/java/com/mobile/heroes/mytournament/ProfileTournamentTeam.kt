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
        val profilePoints = bundle?.get("INTENT_POINTS")
        val profileGoalsDone = bundle?.get("INTENT_GOALS_DONE")
        val profileGoalsReceived = bundle?.get("INTENT_GOALS_RECEIVED")
        val profilePicture = bundle?.get("INTENT_TEAM_PICTURE")

        var profileNameTextView : TextView = findViewById(R.id.tv_tournament_team_profile_name)
        profileNameTextView.setText("$profileName")

        var profilePointsTextView : TextView = findViewById(R.id.tv_tournament_team_profile_points_value)
        profilePointsTextView.setText("$profilePoints")

        var profileGoalsDoneTextView : TextView = findViewById(R.id.tv_tournament_team_profile_goals_value)
        profileGoalsDoneTextView.setText("$profileGoalsDone")

/*        val imageBytes = Base64.decode("$profileIcon",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)*/
        var profilePictureImageView : ImageView = findViewById(R.id.iv_tournament_team_profile_head_image)
        profilePictureImageView.setImageResource(R.drawable.ic_form_tournament_name)

    }

}