package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

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
val songs = ArrayList<Track>()
class SongsHistory(
    val songs: MutableList<Track>
)
val songsHistory = mutableListOf<Track>()