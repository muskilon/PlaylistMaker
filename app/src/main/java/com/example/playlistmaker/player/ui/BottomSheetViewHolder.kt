package com.example.playlistmaker.player.ui

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListSmallSnippetBinding
import com.example.playlistmaker.player.data.db.PlayListEntity

class BottomSheetViewHolder(private val binding: PlayListSmallSnippetBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playList: PlayListEntity) {
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