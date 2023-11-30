package com.example.playlistmaker

import com.example.playlistmaker.MainActivity.Companion.SEARCH_HISTORY_KEY

class SearchHistory(
    val songsHistorySaved: List<Track>
)
object HistoryPreferences {
    val songsHistory = mutableListOf<Track>()
    fun read(): SearchHistory {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return gson.fromJson(json, SearchHistory::class.java)
    }
    fun write(songsHistory: SearchHistory) {
        val json = gson.toJson(songsHistory)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }
    fun clear(){
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }
}