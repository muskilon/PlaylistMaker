package com.example.playlistmaker.medialibrary.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListSnippetBinding
import com.example.playlistmaker.player.data.db.PlayListEntity

class PlayListViewHolder(private val binding: PlayListSnippetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playList: PlayListEntity) {
        val cornerRadius =
            itemView.resources.getDimension(R.dimen.search_snippet_artwork_corner_radius)
        binding.playListTitle.text = playList.title
        binding.playListTrackCount.text = "toDO"
        Glide.with(binding.playListCover)
            .load(playList.cover)
            .placeholder(R.drawable.album_mock)
            .transform(RoundedCorners(cornerRadius.toInt()))
            .into(binding.playListCover)
    }
}