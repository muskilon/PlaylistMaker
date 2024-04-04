package com.example.playlistmaker.player.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_songs_table")
data class PlayListTrackEntity(
    val trackId: String,
    val playListId: Long,
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
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
