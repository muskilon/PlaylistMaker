package com.example.playlistmaker

import android.util.Log
import com.example.playlistmaker.MainActivity.Companion.SEARCH_HISTORY_KEY
import com.google.gson.Gson

interface OnItemClickListener {
    fun onTrackClick(track: Track)
}
val songsHistory = mutableListOf<Track>()
class SearchHistory(
    val songsHistorySaved: List<Track>
)
class HistoryPreferences {
    private val gson = Gson()
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