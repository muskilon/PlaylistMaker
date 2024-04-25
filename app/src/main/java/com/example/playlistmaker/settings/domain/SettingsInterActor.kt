package com.example.playlistmaker.settings.domain

interface SettingsInterActor {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(settings: Boolean)
}