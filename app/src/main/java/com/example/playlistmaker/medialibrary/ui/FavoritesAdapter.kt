package com.example.playlistmaker.medialibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackSnippetBinding
import com.example.playlistmaker.search.domain.Track

class FavoritesAdapter(
    private val tracks: List<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return FavoritesViewHolder(TrackSnippetBinding.inflate(layoutInspector, parent, false))

    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(tracks[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = tracks.size

}
