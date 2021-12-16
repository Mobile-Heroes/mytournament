package com.mobile.heroes.mytournament.feed

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.ProfileTournament
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

class FeedAdapter(private var tournaments: List<TournamentResponse>) :
    RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FeedViewHolder(layoutInflater.inflate(R.layout.item_feed, parent, false))
    }

    override fun getItemCount(): Int {
        return tournaments.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemTitle.text = tournaments[position].name

        var descriptionDisplay = tournaments[position].description!!

        if(descriptionDisplay.length > 80){
            descriptionDisplay = descriptionDisplay.substring(0..80) + "..."
        }

        holder.itemDescription.text = descriptionDisplay

        val dateTime = tournaments[position].startDate!!
        val dateDisplayed = dateTime.dateToString("dd / MMM / yyyy")

        val imageBytes = Base64.decode(tournaments[position].icon,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        holder.itemPicture.setImageBitmap(image)

        holder.itemView.setOnClickListener{

            val intent = Intent(holder.itemView.context, ProfileTournament::class.java)
            intent.putExtra("INTENT_NAME", tournaments[position].name)
            intent.putExtra("INTENT_DESCRIPTION", tournaments[position].description)
            intent.putExtra("INTENT_START_DATE", dateDisplayed)
            intent.putExtra("INTENT_FORMAT", tournaments[position].format)
            intent.putExtra("INTENT_ID", tournaments[position].id)
            intent.putExtra("INTENT_PARTICIPANTS", tournaments[position].participants)
            intent.putExtra("INTENT_MATCHES", tournaments[position].matches)
            intent.putExtra("INTENT_ICON", tournaments[position].icon)
            intent.putExtra("INTENT_STATUS", tournaments[position].status)
            intent.putExtra("INTENT_ORGANIZER", tournaments[position].idUser!!.id)
            holder.itemView.context.startActivity(intent)

        }

    }

    private fun Date.dateToString(format: String):String{
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }

}
