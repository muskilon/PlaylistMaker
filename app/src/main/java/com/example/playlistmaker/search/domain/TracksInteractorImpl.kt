package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    override fun updateHistoryTrack(track: Track) {
        repository.updateHistoryTrack(track)
    }

    override fun getSongsHistory(): MutableList<Track> {
        return repository.getSongsHistory()
    }
    override fun readHistory(): SearchHistory {
        return repository.readHistory()
    }

    override fun writeHistory(songsHistory: SearchHistory) {
        repository.writeHistory(songsHistory)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun setCurrentTrack(currentTrack: Track) {
        repository.setCurrentTrack(currentTrack)
    }

    override fun getSongsStorage(): List<Track> {
        return repository.getSongsStorage()
    }

    override fun setSongsStorage(songs: List<Track>) {
        repository.setSongsStorage(songs)
    }

    override fun searchSongs(
        term: String
    ): Flow<Resource<List<Track>>> {
        return repository.searchSongs(term).map { result ->
            when (result) {
                is Resource.Data -> result
                is Resource.NotFound -> Resource.NotFound("")
                is Resource.ConnectionError -> Resource.ConnectionError("")

            }
        }
    }
}