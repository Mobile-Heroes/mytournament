package com.mobile.heroes.mytournament.feed

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.ProfileTournament
import com.mobile.heroes.mytournament.R

class FeedViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val itemTitle: TextView = itemView.findViewById(R.id.tv_feed_card_title)
    val itemDescription: TextView = itemView.findViewById(R.id.tv_feed_card_description)
    val itemUser: TextView = itemView.findViewById(R.id.tv_feed_card_user)
    val itemStartDate: TextView = itemView.findViewById(R.id.tv_feed_card_start_date)
    val itemPicture: ImageView = itemView.findViewById(R.id.iv_feed_card_image)

    init {
        itemView.setOnClickListener { v: View ->
            val position: Int = adapterPosition
            //Toast.makeText(itemView.context, "Seleccionado en el torneo # ${position + 1}", Toast.LENGTH_SHORT).show()

        }

    }

}