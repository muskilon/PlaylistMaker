package com.example.playlistmaker.domain

interface TracksInteractor {
    fun searchSongs(entity: String, term: String, lang: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundSongs: Resource<List<Track>>)
    }
}