package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.snippetTrackName)
    private val artistName: TextView = itemView.findViewById(R.id.snippetArtistName)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.snippetImage)
    private val trackTime: TextView = itemView.findViewById(R.id.snippetTrackTime)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        val cornerRadius = itemView.context.resources.getDimension(R.dimen.search_snippet_artwork_corner_radius)

        Glide.with(artworkUrl100)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadius.toInt()))
            .into(artworkUrl100)
    }

}