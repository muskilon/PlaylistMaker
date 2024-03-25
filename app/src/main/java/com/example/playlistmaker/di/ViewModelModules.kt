package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.ui.FavoritesViewModel
import com.example.playlistmaker.medialibrary.ui.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel {
        PlayerViewModel(
            currentTrackInteractor = get(),
            mplayer = get(),
            favoritesInteractor = get(),
            tracksInteractor = get()
        )
    }

    viewModel { SearchViewModel(tracksInteractor = get()) }

    viewModel { SettingsViewModel(sharingInteractor = get(), settingsInteractor = get()) }

    viewModel { FavoritesViewModel(get(), get()) }

    viewModel { PlaylistsViewModel() }
}