package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchSongs(term: String): Flow<Resource<List<Track>>>
    fun readHistory(): SearchHistory
    fun writeHistory(songsHistory: SearchHistory)
    fun clearHistory()
    fun setCurrentTrack(currentTrack: Track)
}