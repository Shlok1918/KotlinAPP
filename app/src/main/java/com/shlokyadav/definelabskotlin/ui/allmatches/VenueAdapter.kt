package com.shlokyadav.definelabskotlin.ui.allmatches

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shlokyadav.definelabskotlin.R
import com.shlokyadav.definelabskotlin.VenueDatabaseHelper

class VenueAdapter(
    private val context: Context,
    private var venueList: List<Venue>,
    private val updateListener: UpdateListener?
) : RecyclerView.Adapter<VenueAdapter.VenueViewHolder>() {

    private val dbHelper = VenueDatabaseHelper(context)

    interface UpdateListener {
        fun onUpdate()
    }

    class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.venue_id)
        val nameTextView: TextView = itemView.findViewById(R.id.venue_name)
        val phoneTextView: TextView = itemView.findViewById(R.id.venue_phone)
        val venueLocation: TextView = itemView.findViewById(R.id.venue_location)
        val saveMatches: ImageView = itemView.findViewById(R.id.save_matches)
    }

    fun setVenues(venues: List<Venue>) {
        this.venueList = venues
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.venue_item, parent, false)
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val currentVenue = venueList[position]
        holder.idTextView.text = currentVenue.id
        holder.nameTextView.text = currentVenue.name
        holder.phoneTextView.text = currentVenue.formattedPhone
        holder.venueLocation.text = "${currentVenue.address} ${currentVenue.crossStreet}"

        val isSaved = dbHelper.isVenueSaved(currentVenue.id)
        holder.saveMatches.setImageResource(if (isSaved) R.drawable.star_white else R.drawable.star_border)

        holder.saveMatches.setOnClickListener {
            val isCurrentlySaved = dbHelper.isVenueSaved(currentVenue.id)
            if (isCurrentlySaved) {
                val removed = dbHelper.removeVenue(currentVenue.id)
                if (removed) {
                    holder.saveMatches.setImageResource(R.drawable.star_border)
                    Toast.makeText(context, "Removed from saved venues.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to remove venue.", Toast.LENGTH_SHORT).show()
                }
            } else {
                val isSaved = dbHelper.addVenue(currentVenue)
                if (isSaved) {
                    holder.saveMatches.setImageResource(R.drawable.star_white)
                    Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to save venue.", Toast.LENGTH_SHORT).show()
                }
            }
            updateListener?.onUpdate()
        }
    }

    override fun getItemCount(): Int {
        return venueList.size
    }
}