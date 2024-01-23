package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.search.data.HistorySharedPreferences
import com.example.playlistmaker.search.data.RetrofitNetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.SettingsStorage
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
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

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            SettingsStorage(
                context.getSharedPreferences(
                    MyApplication.SHARED_PREFERENCES,
                    Context.MODE_PRIVATE
                )
            )
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}