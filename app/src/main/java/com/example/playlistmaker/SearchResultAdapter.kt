package com.example.playlistmaker

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track

class SearchResultAdapter(
    private val tracks: List<Track>,
    private val onItemClickListener: ItemClickListener,
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
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_snippet, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce())
                onItemClickListener.onTrackClick(tracks[holder.adapterPosition], this.context)
        }
    }

    override fun getItemCount(): Int = tracks.size

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
