package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

interface FavoritesInteractor {
    suspend fun addSongToFavorites(song: Track)
    suspend fun deleteSongFromFavorites(song: Track)
    fun getFavorites(): List<Track>
    suspend fun updateFavorites()
}