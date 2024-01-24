package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.player.data.CurrentTrackRepositoryImpl
import com.example.playlistmaker.player.domain.CurrentTrackInteractor
import com.example.playlistmaker.player.domain.CurrentTrackInteractorImpl
import com.example.playlistmaker.player.domain.CurrentTrackRepository
import com.example.playlistmaker.search.data.HistorySharedPreferences
import com.example.playlistmaker.search.data.RetrofitNetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.settings.data.ExternalNavigator
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.SettingsStorage
import com.example.playlistmaker.settings.data.SharingInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.SharingInteractor
import com.google.gson.Gson

object Creator {
    fun getGson() = Gson()

    //    TracksInteractor
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

    //    SettingsInteractor
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

    //    SharingInteractor
    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigator(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }

    //    CurrentTrackInteractor
    private fun getCurrentTrackRepository(): CurrentTrackRepository {
        return CurrentTrackRepositoryImpl()
    }

    fun provideCurrentTrackInteractor(): CurrentTrackInteractor {
        return CurrentTrackInteractorImpl(getCurrentTrackRepository())
    }


}