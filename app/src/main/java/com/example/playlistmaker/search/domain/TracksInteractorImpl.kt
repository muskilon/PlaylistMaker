package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

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

    override fun searchSongs(
        entity: String,
        term: String,
        lang: String
    ): Flow<Resource<List<Track>>> {
        return repository.searchSongs(entity, term, lang).map { result ->
            when (result) {
                is Resource.Data -> result
                is Resource.NotFound -> Resource.NotFound("")
                is Resource.ConnectionError -> Resource.ConnectionError("")

            }
        }
    }
}