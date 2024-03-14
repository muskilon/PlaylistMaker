package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface TrackRepository {
    fun searchSongs(entity: String, term: String, lang: String): Flow<Resource<List<Track>>>

    fun readHistory(): SearchHistory
    fun writeHistory(songsHistory: SearchHistory)
    fun clearHistory()
    fun setCurrentTrack(currentTrack: Track)
}