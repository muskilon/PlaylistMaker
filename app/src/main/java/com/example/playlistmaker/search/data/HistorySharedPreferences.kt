package com.example.playlistmaker.search.data

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.SearchHistory

object HistorySharedPreferences {
    private val sharedPreferences = Creator.getSharedPreferences()
    private val gson = Creator.getGson()
    private const val SEARCH_HISTORY_KEY = "searchHistory"
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