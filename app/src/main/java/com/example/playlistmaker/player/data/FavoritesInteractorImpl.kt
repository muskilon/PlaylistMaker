package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.FavoritesInteractor
import com.example.playlistmaker.player.domain.FavoritesRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val repository: FavoritesRepository
) : FavoritesInteractor {
    override fun getFavoritesSongs(): Flow<List<Track>> {
        return repository.getFavoritesSongs()
    }

    override suspend fun addSongToFavorites(song: Track) {
        repository.addSongToFavorites(song)
    }

    override suspend fun deleteSongFromFavorites(song: Track) {
        repository.deleteSongFromFavorites(song)
    }

}