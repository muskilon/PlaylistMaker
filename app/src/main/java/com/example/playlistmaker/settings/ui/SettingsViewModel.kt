package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(
//    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    private var liveIsNightMode = MutableLiveData<Boolean>()

    init {
        liveIsNightMode.value = getThemeSettings()
    }

    fun getThemeState(): LiveData<Boolean> = liveIsNightMode

    private fun getThemeSettings(): Boolean {
        return settingsInteractor.getThemeSettings()
    }

    fun updateThemeSetting(isNightTheme: Boolean) {
        MyApplication.changeTheme(isNightTheme)
        settingsInteractor.updateThemeSetting(isNightTheme)
        liveIsNightMode.value = getThemeSettings()
    }

}