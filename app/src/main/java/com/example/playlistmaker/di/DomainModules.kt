package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.CurrentTrackRepositoryImpl
import com.example.playlistmaker.player.data.FavoritesRepositoryImpl
import com.example.playlistmaker.player.domain.CurrentTrackInteractor
import com.example.playlistmaker.player.domain.CurrentTrackInteractorImpl
import com.example.playlistmaker.player.domain.CurrentTrackRepository
import com.example.playlistmaker.player.domain.FavoritesInteractor
import com.example.playlistmaker.player.domain.FavoritesInteractorImpl
import com.example.playlistmaker.player.domain.FavoritesRepository
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.SharingInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.SharingInteractor
import org.koin.dsl.module

val domainModules = module {
//  Settings
    factory<SharingInteractor> { SharingInteractorImpl(externalNavigator = get()) }
    factory<SettingsInteractor> { SettingsInteractorImpl(repository = get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(settingsStorage = get()) }

//  Search
    factory<TracksInteractor> { TracksInteractorImpl(repository = get()) }
    single<TrackRepository> {
        TrackRepositoryImpl(
            networkClient = get(),
            historySharedPreferences = get()
        )
    }

//  Player
    factory<CurrentTrackInteractor> { CurrentTrackInteractorImpl(repository = get()) }
    single<CurrentTrackRepository> { CurrentTrackRepositoryImpl() }

    factory<FavoritesInteractor> { FavoritesInteractorImpl(repository = get()) }
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(
            appDatabase = get(),
            songsDbConvertor = get()
        )
    }

}