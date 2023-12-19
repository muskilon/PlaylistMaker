package com.example.playlistmaker

import com.example.playlistmaker.data.RetrofitNetworkClient
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}