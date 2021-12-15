package com.mobile.heroes.mytournament.tournamentprofile

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import android.util.Base64
import com.mobile.heroes.mytournament.networking.services.GroupResource.GroupResponse

class TournamentGroupAdapter(private var equipos: List<UserStatsResponse>,  private var teamTournament: List<TeamTournamentResponse>) :
    RecyclerView.Adapter<TournamentGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentGroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentGroupViewHolder(layoutInflater.inflate(R.layout.item_group_table, parent, false))
    }

    override fun getItemCount(): Int {
        return equipos.size
    }

    override fun onBindViewHolder(holder: TournamentGroupViewHolder, position: Int) {

        val goalsDone = teamTournament[position].goalsDone as Int
        val goalsReceived = teamTournament[position].goalsReceived as Int
        val goalsDif = goalsDone - goalsReceived

        val imageBytes = Base64.decode(equipos[position].icon,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        val positionGroup = position + 1

        holder.itemPosition.text = positionGroup.toString()
        holder.itemPicture.setImageBitmap(image)
        holder.itemName.text = equipos[position].nickName
        holder.itemMatches.text = teamTournament[position]!!.countMatches.toString()
        holder.itemGoalsDone.text = goalsDone.toString()
        holder.itemGoalsReceived.text = goalsReceived.toString()
        holder.itemGoalsDif.text = goalsDif.toString()
        holder.itemPoints.text =  teamTournament[position]!!.points.toString()
    }

}