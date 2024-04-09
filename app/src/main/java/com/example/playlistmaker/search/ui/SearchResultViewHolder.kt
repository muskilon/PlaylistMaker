package com.example.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackSnippetBinding
import com.example.playlistmaker.search.domain.Track

class SearchResultViewHolder(private val binding: TrackSnippetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        val cornerRadius =
            itemView.resources.getDimension(R.dimen.search_snippet_artwork_corner_radius)
        binding.snippetTrackName.text = model.trackName
        binding.snippetArtistName.text = model.artistName
        binding.snippetTrackTime.text = model.trackTime
        Glide.with(binding.snippetImage)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadius.toInt()))
            .into(binding.snippetImage)
        binding.snippetArtistName.requestLayout()
    }

}