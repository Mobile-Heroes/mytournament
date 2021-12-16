package com.mobile.heroes.mytournament.ui.checkfields

import android.content.Context
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.NextMatchesAdapter
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse

class SoccerFieldsAdapter(var fields: List<FieldResponse>) :
    RecyclerView.Adapter<SoccerFieldsAdapter.FieldsViewHolder>() {

    inner class FieldsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    lateinit var locationManager: LocationManager

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
//             = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
    }

    override fun getItemCount(): Int {
        return fields.size
    }

}