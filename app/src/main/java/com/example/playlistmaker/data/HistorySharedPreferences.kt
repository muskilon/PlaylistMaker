package com.example.playlistmaker.data

import com.example.playlistmaker.domain.SearchHistory
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.presentation.ui.MainActivity.Companion.SEARCH_HISTORY_KEY
import com.example.playlistmaker.presentation.ui.gson
import com.example.playlistmaker.presentation.ui.sharedPreferences

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