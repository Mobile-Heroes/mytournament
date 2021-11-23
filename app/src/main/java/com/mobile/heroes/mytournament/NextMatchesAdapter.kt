package com.mobile.heroes.mytournament

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.ui.createTournament.upload_image_tournament
import com.mobile.heroes.mytournament.ui.soccerScoreboard.SoccerScoreBoard
import kotlinx.android.synthetic.main.item_match.view.*

class NextMatchesAdapter (var nextMatches: List<NextMatches>, var idListNumber: List<Int>):
    RecyclerView.Adapter<NextMatchesAdapter.MatchesViewHolder>() {

    inner class MatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match,parent,false)
        return MatchesViewHolder(view)
    }

    //Setea la info en el item
    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {

        holder.itemView.apply {
            textViewInfoPartido.text= nextMatches[position].infoDate
            textViewScore.text = nextMatches[position].score
            textViewLocation.text = nextMatches[position].location
            textViewHome.text = nextMatches[position].home
            textViewAway.text = nextMatches[position].away
            var homeLogo = BitmapDrawable(nextMatches[position].logoHome)
            var awayLogo = BitmapDrawable(nextMatches[position].logoAway)
            imageViewHome.setImageDrawable(homeLogo)
            imageViewAway.setImageDrawable(awayLogo)
            cardMatch.setOnClickListener {
                println(idListNumber.get(position))
                val intent = Intent(context, SoccerScoreBoard::class.java)
                intent.putExtra("id",idListNumber.get(position).toString())
                context.startActivity(intent)
            }
        }
    }

    //Cuenta cuantos items hay
    override fun getItemCount(): Int {
        return nextMatches.size
    }
}