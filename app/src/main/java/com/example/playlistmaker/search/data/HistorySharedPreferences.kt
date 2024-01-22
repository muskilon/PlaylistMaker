package com.example.playlistmaker.search.data

import com.example.playlistmaker.main.ui.gson
import com.example.playlistmaker.main.ui.sharedPreferences
import com.example.playlistmaker.search.domain.SearchHistory
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.SearchActivity.Companion.SEARCH_HISTORY_KEY

object HistorySharedPreferences {
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