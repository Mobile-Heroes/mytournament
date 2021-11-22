package com.mobile.heroes.mytournament.join

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.ProfileTournament
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.join.JoinViewHolder
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

class JoinAdapter (private var tournaments: List<TournamentResponse>) :
    RecyclerView.Adapter<JoinViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return JoinViewHolder(layoutInflater.inflate(R.layout.item_join, parent, false))
    }

    override fun getItemCount(): Int {
        return tournaments.size
    }

    override fun onBindViewHolder(holder: JoinViewHolder, position: Int) {
        holder.itemTitle.text = tournaments[position].name
        holder.itemDescription.text = tournaments[position].description
        holder.itemUser.text = ""

        val imageBytes = Base64.decode(tournaments[position].icon,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        holder.itemPicture.setImageBitmap(image)
        //holder.itemPicture.setImageResource(R.drawable.ic_tournament_image)

        holder.itemView.setOnClickListener{
            //Toast.makeText(holder.itemView.context, "Seleccionado en el torneo # ${holder.itemTitle.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, ProfileTournament::class.java)
            intent.putExtra("INTENT_NAME", holder.itemTitle.text)
            intent.putExtra("INTENT_DESCRIPTION", holder.itemDescription.text)
            intent.putExtra("INTENT_START_DATE", tournaments[position].startDate)
            intent.putExtra("INTENT_FORMAT", tournaments[position].format)
            intent.putExtra("INTENT_ID", tournaments[position].id)
            intent.putExtra("INTENT_PARTICIPANTS", tournaments[position].participants)
            intent.putExtra("INTENT_MATCHES", tournaments[position].matches)
            intent.putExtra("INTENT_ICON", tournaments[position].icon)
            holder.itemView.context.startActivity(intent)

        }

    }

}