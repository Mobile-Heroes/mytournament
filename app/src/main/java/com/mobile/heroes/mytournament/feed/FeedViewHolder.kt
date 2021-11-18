package com.mobile.heroes.mytournament.feed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R

class FeedViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val itemTitle: TextView = itemView.findViewById(R.id.tv_feed_card_title)
    val itemDescription: TextView = itemView.findViewById(R.id.tv_feed_card_description)
    val itemUser: TextView = itemView.findViewById(R.id.tv_feed_card_user)
    val itemPicture: ImageView = itemView.findViewById(R.id.iv_feed_card_image)

}