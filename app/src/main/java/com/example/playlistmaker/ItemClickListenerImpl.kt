package com.example.playlistmaker

import android.util.Log
import com.example.playlistmaker.HistoryPreferences.songsHistory

class ItemClickListenerImpl {
    fun onItemClickListener ():OnItemClickListener {
        val onItemClickListener = object : OnItemClickListener {
            override fun onTrackClick(track: Track) {
                when{
                    songsHistory.isEmpty() ->{
                        songsHistory.add(track)
                        HistoryPreferences.write(SearchHistory(songsHistory))
                    }
                    songsHistory.contains(track) -> {
                        songsHistory.remove(track)
                        songsHistory.add(0, track)
                        HistoryPreferences.write(SearchHistory(songsHistory))
                    }
                    songsHistory.size == HISTORY_SIZE -> {
                        songsHistory.removeLast()
                        songsHistory.add(0, track)
                        HistoryPreferences.write(SearchHistory(songsHistory))
                    }
                    else -> {
                        songsHistory.add(0, track)
                        HistoryPreferences.write(SearchHistory(songsHistory))
                    }
                }
                Log.d("TAG", "${songsHistory.size}")
            }
        }
        return onItemClickListener
    }
    companion object{
        private const val HISTORY_SIZE = 10
    }
}