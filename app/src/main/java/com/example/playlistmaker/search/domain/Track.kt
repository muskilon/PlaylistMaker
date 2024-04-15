package com.example.playlistmaker.search.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_songs_table")
data class Track(
    @PrimaryKey
    val trackId: String = "error", // Id трека
    val trackName: String = "error", // Название композиции
    val artistName: String = "error", // Имя исполнителя
    val trackTime: String = "error",
    val artworkUrl100: String = "error", // Ссылка на изображение обложки
    val artworkUrl512: String = "error",
    val previewUrl: String = "error",
    val collectionName: String = "error",
    val country: String = "error",
    val primaryGenreName: String = "error",
    val year: String = "error",
)