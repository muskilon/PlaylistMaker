package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.FavoritesInteractor
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val repository: FavoritesRepositoryImpl
) : FavoritesInteractor {
    override fun getFavoritesSongs(): Flow<List<Track>> {
        return repository.getFavoritesSongs()
    }
}