package com.mobile.heroes.mytournament.tournamentprofile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.ProfileTournament
import com.mobile.heroes.mytournament.ProfileTournamentTeam
import com.mobile.heroes.mytournament.R
import kotlin.random.Random

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
        //holder.itemPicture.setImageResource(images[position])
        holder.itemPicture.setImageResource(R.drawable.ic_form_tournament_name)

        val pointsRandomValue = Random.nextInt(0,30)
        val goalsDoneRandomValue = Random.nextInt(0,15)
        val goalsRecievedRandomValue = Random.nextInt(0,15)

        holder.itemView.setOnClickListener{
            //Toast.makeText(holder.itemView.context, "Seleccionado en el equipo # ${holder.itemName.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, ProfileTournamentTeam::class.java)
            intent.putExtra("INTENT_NAME", holder.itemName.text)
            intent.putExtra("INTENT_POINTS", pointsRandomValue)
            intent.putExtra("INTENT_GOALS_DONE", goalsDoneRandomValue)
            intent.putExtra("INTENT_GOALS_RECEIVED", goalsRecievedRandomValue)
            intent.putExtra("INTENT_TEAM_PICTURE", R.drawable.ic_form_tournament_name)
            holder.itemView.context.startActivity(intent)
        }

    }

}