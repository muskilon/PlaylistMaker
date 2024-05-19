package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.search.domain.Track

data class SinglePlayListState(
    val currentPlayList: PlayList,
    val currentPlayListTracks: List<Track>,
    val totalTime: Int
)
