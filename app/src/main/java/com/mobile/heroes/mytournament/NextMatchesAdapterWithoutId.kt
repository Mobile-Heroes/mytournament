package com.mobile.heroes.mytournament

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.ui.soccerScoreboard.SoccerScoreBoard
import kotlinx.android.synthetic.main.item_match.view.*
import java.text.SimpleDateFormat
import java.util.*

class NextMatchesAdapterWithoutId (var nextMatches: List<NextMatches>):
    RecyclerView.Adapter<NextMatchesAdapterWithoutId.MatchesViewHolder>() {

    inner class MatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match,parent,false)
        return MatchesViewHolder(view)
    }

    //Setea la info en el item
    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {

        holder.itemView.apply {
            textViewInfoPartido.text= nextMatches[position].infoDate.dateToString("dd MMM yyyy")
            textViewScore.text = nextMatches[position].score
            textViewLocation.text = nextMatches[position].location
            textViewHome.text = nextMatches[position].home
            textViewAway.text = nextMatches[position].away
            var homeLogo = BitmapDrawable(nextMatches[position].logoHome)
            var awayLogo = BitmapDrawable(nextMatches[position].logoAway)
            imageViewHome.setImageDrawable(homeLogo)
            imageViewAway.setImageDrawable(awayLogo)
        }

    }

    //Cuenta cuantos items hay
    override fun getItemCount(): Int {
        return nextMatches.size
    }

    private fun Date.dateToString(format: String):String{
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }

}
