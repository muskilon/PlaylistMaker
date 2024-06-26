package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

data class PlayList(
    val id: Long,
    val title: String,
    val description: String? = null,
    val cover: Uri? = null,
    val tracks: TrackList,
    val trackCount: Int = 0
)
