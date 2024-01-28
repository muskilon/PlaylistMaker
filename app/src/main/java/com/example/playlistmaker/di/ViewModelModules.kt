package com.example.playlistmaker.di

import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { PlayerViewModel(currentTrackInteractor = get(), mplayer = get(), handler = get()) }

    viewModel { SearchViewModel(tracksInteractor = get(), handler = get()) }

    viewModel { SettingsViewModel(sharingInteractor = get(), settingsInteractor = get()) }
}