package com.mobile.heroes.mytournament.ui.checkfields

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.NextMatchesAdapter
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import kotlinx.android.synthetic.main.item_fields.view.*

class SoccerFieldsAdapter(var fields: List<FieldResponse>) :
    RecyclerView.Adapter<SoccerFieldsAdapter.FieldsViewHolder>() {

    inner class FieldsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SoccerFieldsAdapter.FieldsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fields, parent, false)
        return FieldsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FieldsViewHolder,
                                  position: Int) {
        holder.itemView.apply {
            tv_soccerfield_team_card_name.text = fields[position].name
            cv_soccer_field_card.setOnClickListener{
                val gmmIntentUri = Uri.parse("geo:${fields[position].lat},${fields[position].lon}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                val title = resources.getString(R.string.chooser_title)
                val chooser = Intent.createChooser(mapIntent, title)
                try {
                    context.startActivity(chooser)
                } catch (e: ActivityNotFoundException) {
                    // Define what your app should do if no activity can handle the intent.
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return fields.size
    }

}