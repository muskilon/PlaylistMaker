package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.cosumer.Resource
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
            when (val result = repository.searchSongs(entity, term, lang)) {
                is Resource.Data -> consumer.consume(result)
                is Resource.NotFound -> consumer.consume(Resource.NotFound(""))
                is Resource.ConnectionError -> consumer.consume(Resource.ConnectionError(""))
            }
            //  consumer.consume(repository.searchSongs(entity, term, lang))
        }
    }
}