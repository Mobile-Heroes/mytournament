package com.mobile.heroes.mytournament.tournamentprofile

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.GroupResource.GroupResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse

class TournamentGroupAdapter (private var grupos: List<GroupResponse>,
                              private var equipos: List<UserStatsResponse>,
                              private var teamTournament: List<TeamTournamentResponse>) :
    RecyclerView.Adapter<TournamentGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentGroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentGroupViewHolder(layoutInflater.inflate(R.layout.item_group_table, parent, false))
    }

    override fun getItemCount(): Int {
        return grupos.size
    }

    override fun onBindViewHolder(holder: TournamentGroupViewHolder, position: Int) {

        holder.itemGroupName.text = grupos[position].name.toString()

        for(i:Int in 0..grupos.size){

            var teamGroupPosition = position * 4

            for(groupRow:Int in 0..3) {

                var groupPosition = groupRow + teamGroupPosition

                val goalsDone = teamTournament[groupPosition].goalsDone as Int
                val goalsReceived = teamTournament[groupPosition].goalsReceived as Int
                val goalsDif = goalsDone - goalsReceived
                val position = groupRow + 1

                when (groupRow) {
                    0 -> {
                        //val imageBytes = Base64.decode(equipos[groupPosition].icon,0)
                        //val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                        //holder.itemPictureRow1.setImageBitmap(image)

                        holder.itemPositionRow1.text = position.toString()
                        holder.itemNameRow1.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow1.text = teamTournament[groupPosition]!!.countMatches.toString()
                        holder.itemGoalsDoneRow1.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow1.text = goalsReceived.toString()
                        holder.itemGoalsDifRow1.text = goalsDif.toString()
                        holder.itemPointsRow1.text =  teamTournament[groupPosition]!!.points.toString()
                    }

                    1 -> {
                        //val imageBytes = Base64.decode(equipos[groupPosition].icon,0)
                        //val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                        //holder.itemPictureRow2.setImageBitmap(image)

                        holder.itemPositionRow2.text = position.toString()
                        holder.itemNameRow2.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow2.text = teamTournament[groupPosition]!!.countMatches.toString()
                        holder.itemGoalsDoneRow2.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow2.text = goalsReceived.toString()
                        holder.itemGoalsDifRow2.text = goalsDif.toString()
                        holder.itemPointsRow2.text =  teamTournament[groupPosition]!!.points.toString()
                    }

                    2 -> {
                        //val imageBytes = Base64.decode(equipos[groupPosition].icon,0)
                        //val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                        //holder.itemPictureRow3.setImageBitmap(image)

                        holder.itemPositionRow3.text = position.toString()
                        holder.itemNameRow3.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow3.text = teamTournament[groupPosition]!!.countMatches.toString()
                        holder.itemGoalsDoneRow3.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow3.text = goalsReceived.toString()
                        holder.itemGoalsDifRow3.text = goalsDif.toString()
                        holder.itemPointsRow3.text =  teamTournament[groupPosition]!!.points.toString()
                    }

                    3 -> {
                        //val imageBytes = Base64.decode(equipos[groupPosition].icon,0)
                        //val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                        //holder.itemPictureRow4.setImageBitmap(image)

                        holder.itemPositionRow4.text = position.toString()
                        holder.itemNameRow4.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow4.text = teamTournament[groupPosition]!!.countMatches.toString()
                        holder.itemGoalsDoneRow4.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow4.text = goalsReceived.toString()
                        holder.itemGoalsDifRow4.text = goalsDif.toString()
                        holder.itemPointsRow4.text =  teamTournament[groupPosition]!!.points.toString()
                    }
                }
            }
        }

        /*val goalsDone = teamTournament[position].goalsDone as Int
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
        holder.itemPoints.text =  teamTournament[position]!!.points.toString()*/
    }

}