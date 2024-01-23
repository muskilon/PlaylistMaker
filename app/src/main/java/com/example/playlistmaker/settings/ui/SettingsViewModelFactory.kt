package com.example.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SettingsViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {
    private val settingsInteractor = Creator.provideSettingsInteractor(context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsInteractor = settingsInteractor) as T
    }
}