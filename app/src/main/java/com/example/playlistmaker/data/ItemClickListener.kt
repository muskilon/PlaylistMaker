package com.example.playlistmaker.data

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.HistorySharedPreferences.songsHistory
import com.example.playlistmaker.domain.models.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.PlayerActivity


class ItemClickListener {
    fun onTrackClick(track: Track, context: Context) {
        when {
            songsHistory.isEmpty() -> {
                songsHistory.add(track)
                HistorySharedPreferences.write(SearchHistory(songsHistory))
            }

            songsHistory.contains(track) -> {
                songsHistory.remove(track)
                songsHistory.add(0, track)
                HistorySharedPreferences.write(SearchHistory(songsHistory))
            }

            songsHistory.size == HISTORY_SIZE -> {
                songsHistory.removeLast()
                songsHistory.add(0, track)
                HistorySharedPreferences.write(SearchHistory(songsHistory))
            }

            else -> {
                songsHistory.add(0, track)
                HistorySharedPreferences.write(SearchHistory(songsHistory))
            }
        }
        val openPlayer = Intent(context, PlayerActivity::class.java)
        openPlayer.putExtra(PlayerActivity.CURRENT_TRACK, track)
        context.startActivity(openPlayer)
    }
    companion object{
        private const val HISTORY_SIZE = 10
    }
}