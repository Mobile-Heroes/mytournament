package com.mobile.heroes.mytournament.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R

class FeedAdapter(private var titles: List<String>, private var descriptions: List<String>,
                  private var organizer:List<String>, var images: List<Int>) :
    RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FeedViewHolder(layoutInflater.inflate(R.layout.item_feed, parent, false))
    }

    override fun getItemCount(): Int {
            return titles.size
        }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDescription.text = descriptions[position]
        holder.itemUser.text = organizer[position]
        holder.itemPicture.setImageResource(images[position])
    }
}