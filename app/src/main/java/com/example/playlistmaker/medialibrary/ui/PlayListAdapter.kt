package com.example.playlistmaker.medialibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlayListSnippetBinding
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.playlist.ui.DiffUtilPlayListCallback

class PlayListAdapter(
    private val onItemClick: (PlayList) -> Unit
) : RecyclerView.Adapter<PlayListViewHolder>() {

    private val playLists = ArrayList<PlayList>()
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

    fun setData(newPlayLists: List<PlayList>) {
        val diffCallback = DiffUtilPlayListCallback(playLists, newPlayLists)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        playLists.clear()
        playLists.addAll(newPlayLists)
        diffResult.dispatchUpdatesTo(this)
    }
}