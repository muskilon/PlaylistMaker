package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.ui.EditPlayListViewModel
import com.example.playlistmaker.medialibrary.ui.FavoritesViewModel
import com.example.playlistmaker.medialibrary.ui.NewPlayListViewModel
import com.example.playlistmaker.medialibrary.ui.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.playlist.ui.SinglePlayListViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel {
        PlayerViewModel(
            currentTrackInterActor = get(),
            mplayer = get(),
            favoritesInterActor = get(),
            playListInterActor = get()
        )
    }

    viewModel { SearchViewModel(tracksInterActor = get()) }

    viewModel { SettingsViewModel(sharingInterActor = get(), settingsInterActor = get()) }

    viewModel { FavoritesViewModel(get(), get()) }

    viewModel { PlaylistsViewModel(get()) }
    viewModel { NewPlayListViewModel(get(), get()) }
    viewModel { SinglePlayListViewModel(get(), get(), get(), get()) }
    viewModel { EditPlayListViewModel(get(), get()) }
}