package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.search.data.HistorySharedPreferences
import com.example.playlistmaker.search.data.RetrofitNetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.google.gson.Gson

object Creator {
    fun getSharedPreferences() = MyApplication.sharedPreferences
    fun getGson() = Gson()
    private fun getTracksRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(context),
            HistorySharedPreferences(
                context.getSharedPreferences(
                    MyApplication.SHARED_PREFERENCES,
                    Context.MODE_PRIVATE
                )
            )
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }
}