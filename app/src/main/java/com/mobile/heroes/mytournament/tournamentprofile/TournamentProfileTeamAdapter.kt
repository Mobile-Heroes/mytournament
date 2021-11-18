package com.mobile.heroes.mytournament.tournamentprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R

class TournamentProfileTeamAdapter(private var names: List<String>, private var images: List<Int>) :
    RecyclerView.Adapter<TournamentProfileTeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentProfileTeamViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentProfileTeamViewHolder(layoutInflater.inflate(R.layout.item_tournament_team, parent, false))
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: TournamentProfileTeamViewHolder, position: Int) {
        holder.itemName.text = names[position]
        holder.itemPicture.setImageResource(images[position])

        holder.itemView.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Seleccionado en el equipo # ${holder.itemName.text}", Toast.LENGTH_SHORT).show()
        }

    }

}