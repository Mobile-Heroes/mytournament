package com.mobile.heroes.mytournament.tournamentprofile

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R

class TournamentTableViewHolder (view: View): RecyclerView.ViewHolder(view) {

    val itemPosition: TextView = itemView.findViewById(R.id.tv_tournament_table_card_position)
    val itemPicture: ImageView = itemView.findViewById(R.id.iv_tournament_table_card_image)
    val itemName: TextView = itemView.findViewById(R.id.tv_tournament_table_card_name)
    val itemGoalsDone: TextView = itemView.findViewById(R.id.tv_tournament_table_card_goals_done)
    val itemGoalsReceived: TextView = itemView.findViewById(R.id.tv_tournament_table_card_goals_received)
    val itemGoalsDif: TextView = itemView.findViewById(R.id.tv_tournament_table_card_goals_dif)
    val itemPoints: TextView = itemView.findViewById(R.id.tv_tournament_table_card_points)

}