package com.example.playlistmaker.settings.data

import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsStorage: SettingsStorage
) : SettingsRepository {
    override fun getThemeSettings(): Boolean {
        return settingsStorage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: Boolean) {
        settingsStorage.updateThemeSetting(settings)
    }
}