package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.player.data.db.PlayListEntity
import com.example.playlistmaker.search.domain.Track

interface PlayListRepository {
    suspend fun addTrackToPlayList(track: Track, playListId: Long)

    suspend fun addPlayList(playList: PlayListEntity)

    suspend fun updatePlayListsList()

    fun getPlayLists(): List<PlayListEntity>
}