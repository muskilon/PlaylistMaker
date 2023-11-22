package com.example.playlistmaker

import android.util.Log

class ItemClickListenerImpl {
    fun onItemClickListener ():OnItemClickListener {
        val onItemClickListener = object : OnItemClickListener {
            override fun onTrackClick(track: Track) {
                val historyPreferences = HistoryPreferences()
                when{
                    songsHistory.isEmpty() ->{
                        songsHistory.add(track)
                        historyPreferences.write(SearchHistory(songsHistory))
                    }
                    songsHistory.contains(track) -> {
                        songsHistory.remove(track)
                        songsHistory.add(0, track)
                        historyPreferences.write(SearchHistory(songsHistory))
                    }
                    songsHistory.size == 10 -> {
                        songsHistory.removeLast()
                        songsHistory.add(0, track)
                        historyPreferences.write(SearchHistory(songsHistory))
                    }
                    else -> {
                        songsHistory.add(0, track)
                        historyPreferences.write(SearchHistory(songsHistory))
                    }
                }
                Log.d("TAG", "${songsHistory.size}")
            }
        }
        return onItemClickListener
    }
}