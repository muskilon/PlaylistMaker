package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

class FavoritesInteractorImpl(
    private val repository: FavoritesRepository
) : FavoritesInteractor {

    override suspend fun addSongToFavorites(song: Track) {
        repository.addSongToFavorites(song)
    }

    override suspend fun deleteSongFromFavorites(song: Track) {
        repository.deleteSongFromFavorites(song)
    }

    override fun getFavorites(): List<Track> {
        return repository.getFavorites()
    }

    override suspend fun updateFavorites() {
        repository.updateFavorites()
    }

}