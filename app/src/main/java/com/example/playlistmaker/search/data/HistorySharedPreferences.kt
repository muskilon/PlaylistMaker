package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.SearchHistory

class HistorySharedPreferences(private val sharedPreferences: SharedPreferences) {
    private val gson = Creator.getGson()
    fun readHistory(): SearchHistory {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (json == null) SearchHistory(emptyList())
        else gson.fromJson(json, SearchHistory::class.java)
    }

    fun writeHistory(songsHistory: SearchHistory) {
        val json = gson.toJson(songsHistory)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun clearHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "searchHistory"
    }
}