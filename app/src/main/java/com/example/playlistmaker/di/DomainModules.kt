package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.data.FilesRepositoryImpl
import com.example.playlistmaker.medialibrary.data.PlayListRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.FilesInterActor
import com.example.playlistmaker.medialibrary.domain.FilesInterActorImpl
import com.example.playlistmaker.medialibrary.domain.FilesRepository
import com.example.playlistmaker.medialibrary.domain.PlayListInterActor
import com.example.playlistmaker.medialibrary.domain.PlayListInterActorImpl
import com.example.playlistmaker.medialibrary.domain.PlayListRepository
import com.example.playlistmaker.player.data.CurrentTrackRepositoryImpl
import com.example.playlistmaker.player.data.FavoritesRepositoryImpl
import com.example.playlistmaker.player.domain.CurrentTrackInterActor
import com.example.playlistmaker.player.domain.CurrentTrackInterActorImpl
import com.example.playlistmaker.player.domain.CurrentTrackRepository
import com.example.playlistmaker.player.domain.FavoritesInterActor
import com.example.playlistmaker.player.domain.FavoritesInterActorImpl
import com.example.playlistmaker.player.domain.FavoritesRepository
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TracksInterActor
import com.example.playlistmaker.search.domain.TracksInterActorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.SharingInterActorImpl
import com.example.playlistmaker.settings.domain.SettingsInterActor
import com.example.playlistmaker.settings.domain.SettingsInterActorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.SharingInterActor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModules = module {
//  Settings
    factory<SharingInterActor> { SharingInterActorImpl(externalNavigator = get()) }
    factory<SettingsInterActor> { SettingsInterActorImpl(repository = get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(settingsStorage = get()) }

//  Search
    factory<TracksInterActor> { TracksInterActorImpl(repository = get()) }
    single<TrackRepository> {
        TrackRepositoryImpl(
            networkClient = get(),
            historySharedPreferences = get()
        )
    }

//  Player
    factory<CurrentTrackInterActor> { CurrentTrackInterActorImpl(repository = get()) }
    single<CurrentTrackRepository> { CurrentTrackRepositoryImpl() }

    factory<FavoritesInterActor> { FavoritesInterActorImpl(repository = get()) }
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(
            appDatabase = get(),
            songsDbConvertor = get()
        )
    }
//Medialibrary
    factory<FilesInterActor> { FilesInterActorImpl(repository = get()) }
    single<FilesRepository> { FilesRepositoryImpl(context = androidContext()) }
    factory<PlayListInterActor> { PlayListInterActorImpl(repository = get()) }
    single<PlayListRepository> {
        PlayListRepositoryImpl(
            appDatabase = get(),
            playListDbConvertor = get()
        )
    }

}