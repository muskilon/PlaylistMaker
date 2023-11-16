package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

data class Track(
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

