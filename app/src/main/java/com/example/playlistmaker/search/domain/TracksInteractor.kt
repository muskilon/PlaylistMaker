package com.example.playlistmaker.search.domain

interface TracksInteractor {
    fun searchSongs(entity: String, term: String, lang: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundSongs: Resource<List<Track>>)
    }

    fun readHistory(): SearchHistory
    fun writeHistory(songsHistory: SearchHistory)
    fun clearHistory()
}