package com.example.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackSnippetBinding
import com.example.playlistmaker.search.domain.Track

class SinglePlayListAdapter(
    private val tracks: List<Track>,
    private val onItemClick: (Track, Boolean) -> Unit
) : RecyclerView.Adapter<SinglePlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglePlayListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return SinglePlayListViewHolder(TrackSnippetBinding.inflate(layoutInspector, parent, false))

    }

    override fun onBindViewHolder(holder: SinglePlayListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(tracks[holder.adapterPosition], false)
        }
        holder.itemView.setOnLongClickListener {
            onItemClick.invoke(tracks[holder.adapterPosition], true)

            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = tracks.size

}
