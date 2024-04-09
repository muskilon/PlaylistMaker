package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.player.data.db.PlayListEntity
import com.example.playlistmaker.search.domain.Track

interface PlayListInteractor {
    suspend fun addTrackToPlayList(track: Track, playListId: Long)

    suspend fun addPlayList(playList: PlayListEntity)

    fun getPlayLists(): List<PlayListEntity>

    suspend fun updatePlayLists()
}