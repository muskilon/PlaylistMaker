package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchSongs(
        entity: String,
        term: String,
        lang: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            consumer.consume(repository.searchSongs(entity, term, lang))
        }
    }
}