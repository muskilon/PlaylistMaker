package com.example.playlistmaker.medialibrary.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListSnippetBinding
import com.example.playlistmaker.medialibrary.domain.PlayList

class PlayListViewHolder(private val binding: PlayListSnippetBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playList: PlayList) {
        val cornerRadius =
            itemView.resources.getDimension(R.dimen.search_snippet_artwork_corner_radius)
        binding.playListTitle.text = playList.title
        binding.playListTrackCount.text = MyApplication.getAppResources()
            .getQuantityString(R.plurals.track_plurals, playList.trackCount, playList.trackCount)
        Glide.with(binding.playListCover)
            .load(playList.cover)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadius.toInt()))
            .into(binding.playListCover)
    }
}