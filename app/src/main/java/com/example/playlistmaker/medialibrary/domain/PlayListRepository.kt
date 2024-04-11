package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.search.domain.Track

interface PlayListRepository {
    suspend fun addTrackToPlayList(playList: PlayList, track: Track)

    suspend fun addPlayList(playList: PlayList)

    suspend fun updatePlayListsList()

    fun getPlayLists(): List<PlayList>
}