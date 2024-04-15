package com.example.playlistmaker.medialibrary.domain

import com.example.playlistmaker.search.domain.Track

class PlayListInteractorImpl(
    private val repository: PlayListRepository
) : PlayListInteractor {
    override suspend fun addPlayList(playList: PlayList) {
        repository.addPlayList(playList)
    }

    override suspend fun addTrackToPlayList(playList: PlayList, track: Track) {
        repository.addTrackToPlayList(playList, track)
    }

    override fun getPlayLists(): List<PlayList> {
        return repository.getPlayLists()
    }

    override suspend fun updatePlayLists() {
        repository.updatePlayListsList()
    }

    override suspend fun deletePlayList(playList: PlayList) {
        repository.deletePlayList(playList)
    }
}