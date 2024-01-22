package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackSnippetBinding
import com.example.playlistmaker.player.domain.TrackModelInteractor
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.Track

class SearchResultAdapter(
    private val tracks: List<Track>,
    private val viewModel: SearchViewModel,
    private val context: Context
) : RecyclerView.Adapter<SearchResultViewHolder>() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchResultViewHolder(TrackSnippetBinding.inflate(layoutInspector, parent, false))

    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce())
                viewModel.onTrackClick(tracks[holder.adapterPosition])
            TrackModelInteractor.setTrackModel(tracks[holder.adapterPosition])
            val openPlayer = Intent(context, PlayerActivity::class.java)
            context.startActivity(openPlayer)
        }
    }

    override fun getItemCount(): Int = tracks.size

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
