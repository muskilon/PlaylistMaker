package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log

interface OnItemClickListener {
    fun onTrackClick(track: Track)
}
fun onItemClickListener (sharedPreferences: SharedPreferences):OnItemClickListener {
    val onItemClickListener = object : OnItemClickListener {
        override fun onTrackClick(track: Track) {
            val historyPreferences = HistoryPreferences()

            if (songsHistory.isEmpty()) {
                songsHistory.add(track)
                historyPreferences.write(sharedPreferences, SearchHistory(songsHistory))
            } else
                if (songsHistory.contains(track)) {
                    songsHistory.remove(track)
                    songsHistory.add(0, track)
                    historyPreferences.write(sharedPreferences, SearchHistory(songsHistory))
                } else if (songsHistory.size == 10) {
                    songsHistory.removeAt(songsHistory.size - 1)
                    songsHistory.add(0, track)
                    historyPreferences.write(sharedPreferences, SearchHistory(songsHistory))
                } else {
                    songsHistory.add(0, track)

                    historyPreferences.write(sharedPreferences, SearchHistory(songsHistory))
                }
            Log.d("TAG", "${songsHistory.size}${songsHistory}")
        }
    }
    return onItemClickListener
}