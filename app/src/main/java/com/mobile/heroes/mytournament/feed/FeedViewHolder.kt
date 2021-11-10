package com.mobile.heroes.mytournament.feed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.R

class FeedViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val itemTitle: TextView = itemView.findViewById(R.id.tv_feed_card_title)
    val itemDescription: TextView = itemView.findViewById(R.id.tv_feed_card_description)
    val itemUser: TextView = itemView.findViewById(R.id.tv_feed_card_user)
    val itemPicture: ImageView = itemView.findViewById(R.id.iv_feed_card_image)

    init {
        itemView.setOnClickListener { v: View ->
            val position: Int = adapterPosition
            Toast.makeText(itemView.context, "Seleccionado en el torneo # ${position + 1}", Toast.LENGTH_SHORT).show()
        }

    }
}