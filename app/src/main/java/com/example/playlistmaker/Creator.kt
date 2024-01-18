package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.RetrofitNetworkClient
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }
}