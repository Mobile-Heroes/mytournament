package com.mobile.heroes.mytournament.tournamentprofile

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R

class TournamentProfileTeamViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val itemPicture: ImageView = itemView.findViewById(R.id.iv_tournament_team_card_image)
    val itemName: TextView = itemView.findViewById(R.id.tv_tournament_team_card_name)
    val itemTitles: TextView = itemView.findViewById(R.id.tv_tournament_team_card_titles_value)

}