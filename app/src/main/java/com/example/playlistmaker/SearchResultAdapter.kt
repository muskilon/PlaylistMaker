package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.PlayerActivity.Companion.CURRENT_TRACK

class SearchResultAdapter(
    private val tracks: List<Track>,
    private val onItemClickListener: OnItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<SearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_snippet, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(tracks[position])
        val currentTrack = tracks[position]
        holder.itemView.setOnClickListener {
            onItemClickListener.onTrackClick(tracks[holder.adapterPosition])
            val openPlayer = Intent(this.context, PlayerActivity::class.java)
            openPlayer.putExtra(CURRENT_TRACK, currentTrack)
            it.context.startActivity(openPlayer)
        }
    }

    override fun getItemCount(): Int = tracks.size
}
