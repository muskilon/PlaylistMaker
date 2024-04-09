package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.player.data.db.PlayListEntity
import com.example.playlistmaker.search.domain.Track

class PlayListInteractorImpl(
    private val repository: PlayListRepository
) : PlayListInteractor {
    override suspend fun addPlayList(playList: PlayListEntity) {
        repository.addPlayList(playList)
    }

    override suspend fun addTrackToPlayList(track: Track, playListId: Long) {
        repository.addTrackToPlayList(track, playListId)
    }

    override fun getPlayLists(): List<PlayListEntity> {
        return repository.getPlayLists()
    }

    override suspend fun updatePlayLists() {
        repository.updatePlayListsList()
    }
}