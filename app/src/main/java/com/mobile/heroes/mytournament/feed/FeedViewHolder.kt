package com.mobile.heroes.mytournament.feed

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.databinding.ItemFeedBinding
import com.squareup.picasso.Picasso

class FeedViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemFeedBinding.bind(view)

    fun bind(image:String){
        Picasso.get().load(image).into(binding.ivFeedCardImage)
    }
}