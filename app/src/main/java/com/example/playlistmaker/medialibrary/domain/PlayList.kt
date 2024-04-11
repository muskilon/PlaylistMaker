package com.example.playlistmaker.medialibrary.domain

data class PlayList(
    val id: Long,
    val title: String,
    val description: String?,
    val cover: String?,
    val tracks: TrackList,
    val trackCount: Int = 0
)
