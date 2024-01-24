package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackModel

interface TrackRepository {
    fun searchSongs(entity: String, term: String, lang: String): Resource<List<Track>>

    fun readHistory(): SearchHistory
    fun writeHistory(songsHistory: SearchHistory)
    fun clearHistory()
    fun getTrackModel(): TrackModel
    fun setTrackModel(currentTrack: Track)
}