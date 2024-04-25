package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.settings.domain.SettingsInterActor
import com.example.playlistmaker.settings.domain.SharingInterActor

class SettingsViewModel(
    private val sharingInterActor: SharingInterActor,
    private val settingsInterActor: SettingsInterActor
) : ViewModel() {
    private var liveIsNightMode = MutableLiveData<Boolean>()

    init {
        liveIsNightMode.value = getThemeSettings()
    }

    fun getThemeState(): LiveData<Boolean> = liveIsNightMode

    private fun getThemeSettings(): Boolean {
        return settingsInterActor.getThemeSettings()
    }

    fun updateThemeSetting(isNightTheme: Boolean) {
        MyApplication.changeTheme(isNightTheme)
        settingsInterActor.updateThemeSetting(isNightTheme)
        liveIsNightMode.value = getThemeSettings()
    }

    fun openTerms() {
        sharingInterActor.openTerms()
    }

    fun shareApp() {
        sharingInterActor.shareApp()
    }

    fun openSupport() {
        sharingInterActor.openSupport()
    }

}