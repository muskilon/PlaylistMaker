package com.example.playlistmaker.search.domain

import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun readHistory(): SearchHistory {
        return repository.readHistory()
    }

    override fun writeHistory(songsHistory: SearchHistory) {
        repository.writeHistory(songsHistory)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun searchSongs(
        entity: String,
        term: String,
        lang: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            when (val result = repository.searchSongs(entity, term, lang)) {
                is Resource.Data -> consumer.consume(result)
                is Resource.NotFound -> consumer.consume(Resource.NotFound(""))
                is Resource.ConnectionError -> consumer.consume(Resource.ConnectionError(""))
            }
        }
    }
}