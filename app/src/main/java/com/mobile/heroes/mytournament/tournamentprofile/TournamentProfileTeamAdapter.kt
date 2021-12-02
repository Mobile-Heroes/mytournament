package com.mobile.heroes.mytournament.tournamentprofile

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.ProfileTournament
import com.mobile.heroes.mytournament.ProfileTournamentTeam
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import kotlin.random.Random

class TournamentProfileTeamAdapter(private var equipos: List<UserStatsResponse>, private var idTournament: Int) :
    RecyclerView.Adapter<TournamentProfileTeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentProfileTeamViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentProfileTeamViewHolder(layoutInflater.inflate(R.layout.item_tournament_team, parent, false))
    }

    override fun getItemCount(): Int {
        return equipos.size
    }

    override fun onBindViewHolder(holder: TournamentProfileTeamViewHolder, position: Int) {
        holder.itemName.text = equipos[position].nickName
        //holder.itemPicture.setImageResource(R.drawable.ic_form_tournament_name)

        val imageBytes = Base64.decode(equipos[position].icon,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        holder.itemPicture.setImageBitmap(image)

        val pointsRandomValue = Random.nextInt(0,30)
        val goalsDoneRandomValue = Random.nextInt(0,15)
        val goalsRecievedRandomValue = Random.nextInt(0,15)

        holder.itemView.setOnClickListener{
            //Toast.makeText(holder.itemView.context, "Seleccionado en el equipo # ${holder.itemName.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, ProfileTournamentTeam::class.java)
            intent.putExtra("INTENT_NICK_NAME", equipos[position].nickName)
            intent.putExtra("INTENT_GOALS", equipos[position].goals)
            intent.putExtra("INTENT_TITLES", equipos[position].titles)
            intent.putExtra("INTENT_ID", "$idTournament")

            intent.putExtra("INTENT_TOURNAMENTS", equipos[position].tournaments)
            intent.putExtra("INTENT_TEAM_PICTURE", equipos[position].icon)
            intent.putExtra("INTENT_ID_USER", equipos[position].idUser.toString())
            holder.itemView.context.startActivity(intent)
        }

    }

}