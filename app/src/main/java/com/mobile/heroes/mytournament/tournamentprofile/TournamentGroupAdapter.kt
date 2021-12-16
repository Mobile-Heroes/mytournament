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

class TournamentGroupAdapter (private var grupos: List<TeamInGroupDTO>,
                              private var equipos: List<UserStatsResponse>
                              ) :
    RecyclerView.Adapter<TournamentGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentGroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentGroupViewHolder(layoutInflater.inflate(R.layout.item_group_table, parent, false))
    }

    override fun getItemCount(): Int {
        return grupos.size
    }

    override fun onBindViewHolder(holder: TournamentGroupViewHolder, position: Int) {

        holder.itemGroupName.text = grupos[position].groupDTO.name.toString()

        for(i:Int in 0..grupos.size){

            var teamGroupPosition = position * 4

            val teamList = grupos[position].teamTournamentDTOList

            for(groupRow:Int in 0..teamList!!.size-1) {

                var groupPosition = groupRow + teamGroupPosition

                val goalsDone = teamList[groupRow].goalsDone as Int
                val goalsReceived = teamList[groupRow].goalsReceived as Int
                val goalsDif = goalsDone - goalsReceived
                val position = groupRow + 1

                val imageBytes = Base64.decode(equipos[groupPosition].icon,0)
                val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)

                when (groupRow) {
                    0 -> {
                        holder.itemPositionRow1.text = position.toString()
                        holder.itemPictureRow1.setImageBitmap(image)
                        holder.itemNameRow1.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow1.text = teamList[groupRow]!!.countMatches.toString()
                        holder.itemGoalsDoneRow1.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow1.text = goalsReceived.toString()
                        holder.itemGoalsDifRow1.text = goalsDif.toString()
                        holder.itemPointsRow1.text =  teamList[groupRow]!!.points.toString()
                    }

                    1 -> {
                        holder.itemPositionRow2.text = position.toString()
                        holder.itemPictureRow2.setImageBitmap(image)
                        holder.itemNameRow2.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow2.text = teamList[groupRow]!!.countMatches.toString()
                        holder.itemGoalsDoneRow2.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow2.text = goalsReceived.toString()
                        holder.itemGoalsDifRow2.text = goalsDif.toString()
                        holder.itemPointsRow2.text =  teamList[groupRow]!!.points.toString()
                    }

                    2 -> {
                        holder.itemPositionRow3.text = position.toString()
                        holder.itemPictureRow3.setImageBitmap(image)
                        holder.itemNameRow3.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow3.text = teamList[groupRow]!!.countMatches.toString()
                        holder.itemGoalsDoneRow3.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow3.text = goalsReceived.toString()
                        holder.itemGoalsDifRow3.text = goalsDif.toString()
                        holder.itemPointsRow3.text =  teamList[groupRow]!!.points.toString()
                    }

                    3 -> {
                        holder.itemPositionRow4.text = position.toString()
                        holder.itemPictureRow4.setImageBitmap(image)
                        holder.itemNameRow4.text = equipos[groupPosition].nickName.toString()
                        holder.itemMatchesRow4.text = teamList[groupRow]!!.countMatches.toString()
                        holder.itemGoalsDoneRow4.text = goalsDone.toString()
                        holder.itemGoalsReceivedRow4.text = goalsReceived.toString()
                        holder.itemGoalsDifRow4.text = goalsDif.toString()
                        holder.itemPointsRow4.text =  teamList[groupRow]!!.points.toString()
                    }
                }
            }
        }
    }

}