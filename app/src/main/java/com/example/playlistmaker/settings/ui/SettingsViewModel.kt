package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(
//    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    fun getThemeSettings(): Boolean {
        return settingsInteractor.getThemeSettings()
    }

    fun updateThemeSetting(settings: Boolean) {
        settingsInteractor.updateThemeSetting(settings)
    }

}