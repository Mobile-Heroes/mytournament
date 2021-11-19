package com.mobile.heroes.mytournament.tournamentprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R
import kotlin.random.Random

class TournamentTableAdapter(private var posicion: List<String>, private var names: List<String>) :
    RecyclerView.Adapter<TournamentTableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentTableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentTableViewHolder(layoutInflater.inflate(R.layout.item_table_row, parent, false))
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: TournamentTableViewHolder, position: Int) {

        val goalsDoneRandomValue = Random.nextInt(0,15)
        val goalsRecievedRandomValue = Random.nextInt(0,15)
        val goalsDif = goalsDoneRandomValue - goalsRecievedRandomValue
        val pointsRandomValue = Random.nextInt(0,30)

        //holder.itemPicture.setImageResource(images[position])
        holder.itemPosition.text = posicion[position]
        holder.itemPicture.setImageResource(R.drawable.ic_form_tournament_name)
        holder.itemName.text = names[position]
        holder.itemGoalsDone.text = goalsDoneRandomValue.toString()
        holder.itemGoalsReceived.text = goalsRecievedRandomValue.toString()
        holder.itemGoalsDif.text = goalsDif.toString()
        holder.itemPoints.text =  pointsRandomValue.toString()

    }

}