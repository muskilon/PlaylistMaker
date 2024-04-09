package com.example.playlistmaker.medialibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlayListSnippetBinding
import com.example.playlistmaker.player.data.db.PlayListEntity

class PlayListAdapter(
    private val playLists: List<PlayListEntity>,
    private val onItemClick: (PlayListEntity) -> Unit
) : RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayListViewHolder(PlayListSnippetBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playLists[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(playLists[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return playLists.size
    }
}