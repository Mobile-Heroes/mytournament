package com.mobile.heroes.mytournament.tournamentprofile

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import kotlin.random.Random

class TournamentTableAdapter(private var posicion: List<String>, private var equipos: List<UserStatsResponse>,  private var teamTournament: List<TeamTournamentResponse>) :
    RecyclerView.Adapter<TournamentTableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentTableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentTableViewHolder(layoutInflater.inflate(R.layout.item_table_row, parent, false))
    }

    override fun getItemCount(): Int {
        return equipos.size
    }

    override fun onBindViewHolder(holder: TournamentTableViewHolder, position: Int) {

        val goalsDone = teamTournament[position].goalsDone as Int
        val goalsReceived = teamTournament[position].goalsReceived as Int
        val goalsDif = goalsDone - goalsReceived

        val imageBytes = Base64.decode(equipos[position].icon,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)

        holder.itemPosition.text = posicion[position]
        holder.itemPicture.setImageBitmap(image)
        holder.itemName.text = equipos[position].nickName
        holder.itemGoalsDone.text = goalsDone.toString()
        holder.itemGoalsReceived.text = goalsReceived.toString()
        holder.itemGoalsDif.text = goalsDif.toString()
        holder.itemPoints.text =  teamTournament[position]!!.points.toString()

    }

}