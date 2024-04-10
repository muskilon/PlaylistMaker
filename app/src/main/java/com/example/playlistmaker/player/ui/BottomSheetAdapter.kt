package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlayListSmallSnippetBinding
import com.example.playlistmaker.player.data.db.PlayListEntity

class BottomSheetAdapter(
    private val playLists: List<PlayListEntity>,
    private val onItemClick: (PlayListEntity) -> Unit
) : RecyclerView.Adapter<BottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return BottomSheetViewHolder(
            PlayListSmallSnippetBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(playLists[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(playLists[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return playLists.size
    }
}