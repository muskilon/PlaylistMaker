package com.example.playlistmaker

import com.example.playlistmaker.MainActivity.Companion.SEARCH_HISTORY_KEY
import com.google.gson.Gson

interface OnItemClickListener {
    fun onTrackClick(track: Track)
}
//val songsHistory = mutableListOf<Track>()
class SearchHistory(
    val songsHistorySaved: List<Track>
)
object HistoryPreferences {
    private val gson = Gson()
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