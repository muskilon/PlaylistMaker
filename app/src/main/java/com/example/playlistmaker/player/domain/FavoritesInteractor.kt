package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun getFavoritesSongs(): Flow<List<Track>>
}