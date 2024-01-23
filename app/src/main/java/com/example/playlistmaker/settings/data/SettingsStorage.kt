package com.example.playlistmaker.settings.data

import android.content.SharedPreferences

class SettingsStorage(private val sharedPreferences: SharedPreferences) {
    fun getThemeSettings(): Boolean {
        return sharedPreferences.getBoolean(NIGHT_MODE_KEY, false)
    }

    fun updateThemeSetting(settings: Boolean) {
        sharedPreferences.edit()
            .putBoolean(NIGHT_MODE_KEY, settings)
            .apply()
    }

    companion object {
        const val NIGHT_MODE_KEY = "nightMode"
    }
}