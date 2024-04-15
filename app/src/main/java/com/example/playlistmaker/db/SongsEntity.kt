package com.example.playlistmaker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs_table")
data class SongsEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val previewUrl: String,
    val collectionName: String,
    val country: String,
    val primaryGenreName: String,
    val year: String
)
