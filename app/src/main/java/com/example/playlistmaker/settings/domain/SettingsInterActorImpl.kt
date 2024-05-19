package com.example.playlistmaker.settings.domain

class SettingsInterActorImpl(private val repository: SettingsRepository) : SettingsInterActor {
    override fun getThemeSettings(): Boolean {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: Boolean) {
        return repository.updateThemeSetting(settings)
    }
}