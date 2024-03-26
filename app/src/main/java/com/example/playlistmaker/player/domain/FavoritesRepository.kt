package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoritesSongs(): Flow<List<Track>>
    suspend fun addSongToFavorites(song: Track)
    suspend fun deleteSongFromFavorites(song: Track)
}