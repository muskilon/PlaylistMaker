package com.example.playlistmaker.medialibrary.ui

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListSnippetBinding
import com.example.playlistmaker.medialibrary.domain.PlayList

class PlayListViewHolder(private val binding: PlayListSnippetBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playList: PlayList) {
        val uri = Uri.parse(Uri.decode(playList.cover))
        val cornerRadius =
            itemView.resources.getDimension(R.dimen.search_snippet_artwork_corner_radius)
        binding.playListTitle.text = playList.title
        binding.playListTrackCount.text = playList.trackCount.toString()
        Glide.with(binding.playListCover)
            .load(uri)
            .placeholder(R.drawable.placeholder_big)
            .transform(RoundedCorners(cornerRadius.toInt()))
            .into(binding.playListCover)
    }
}