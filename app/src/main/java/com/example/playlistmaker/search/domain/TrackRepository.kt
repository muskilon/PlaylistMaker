package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface TrackRepository {
    fun searchSongs(term: String): Flow<Resource<List<Track>>>
    fun readHistory(): SearchHistory
    fun writeHistory(songsHistory: SearchHistory)
    fun clearHistory()
    fun setCurrentTrack(currentTrack: Track)
    fun setSongsStorage(songs: List<Track>)
    fun getSongsStorage(): List<Track>
    fun getSongsHistory(): MutableList<Track>
    fun updateHistoryTrack(track: Track)
}