package com.example.playlistmaker

import com.google.gson.Gson

interface OnItemClickListener {
    fun onTrackClick(track: Track)
}
val songsHistory = mutableListOf<Track>()
class SearchHistory(
    val songsHistorySaved: MutableList<Track>
)
class HistoryPreferences {
    fun read(): SearchHistory {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return Gson().fromJson(json, SearchHistory::class.java)
    }

    fun write(songsHistory: SearchHistory) {
        val json = Gson().toJson(songsHistory)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }
}
fun onItemClickListener ():OnItemClickListener {
    val onItemClickListener = object : OnItemClickListener {
        override fun onTrackClick(track: Track) {
            val historyPreferences = HistoryPreferences()

            if (songsHistory.isEmpty()) {
                songsHistory.add(track)
                historyPreferences.write(SearchHistory(songsHistory))
            } else
                if (songsHistory.contains(track)) {
                    songsHistory.remove(track)
                    songsHistory.add(0, track)
                    historyPreferences.write(SearchHistory(songsHistory))
                } else if (songsHistory.size == 10) {
                    songsHistory.removeAt(songsHistory.size - 1)
                    songsHistory.add(0, track)
                    historyPreferences.write(SearchHistory(songsHistory))
                } else {
                    songsHistory.add(0, track)

                    historyPreferences.write(SearchHistory(songsHistory))
                }
        }
    }
    return onItemClickListener
}