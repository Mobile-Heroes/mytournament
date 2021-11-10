package com.mobile.heroes.mytournament

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_match.view.*

class NextMatchesAdapter (var nextMatches: List<NextMatches>):
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
        }
    }

    //Cuenta cuantos items hay
    override fun getItemCount(): Int {
        return nextMatches.size
    }
}