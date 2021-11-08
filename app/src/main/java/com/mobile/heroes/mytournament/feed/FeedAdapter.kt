package com.mobile.heroes.mytournament.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R

class FeedAdapter(private val images: List<String>) : RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FeedViewHolder(layoutInflater.inflate(R.layout.item_feed, parent, false))
    }

    override fun getItemCount(): Int = images.size


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = images[position]
        holder.bind(item)
    }
}