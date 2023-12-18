package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.cosumer.Resource
import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchSongs(entity: String, term: String, lang: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundSongs: Resource<List<Track>>)
    }
}