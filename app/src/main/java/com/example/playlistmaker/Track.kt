package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: String, // Id трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
){
    var trackTime: String = "--:--"
        get(){return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.trackTimeMillis) }
}

class SearchResponse(
    val results: ArrayList<Track>,
    val resultCount: Int
)
class HistoryPreferences {
    fun read(sharedPreferences: SharedPreferences): SearchHistory {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return Gson().fromJson(json, SearchHistory::class.java)
    }

    fun write(sharedPreferences: SharedPreferences, songsHistory: SearchHistory) {
        val json = Gson().toJson(songsHistory)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }
}
class SearchHistory(
    val songsHistorySaved: MutableList<Track>
)
val songs = ArrayList<Track>()
val songsHistory = mutableListOf<Track>()