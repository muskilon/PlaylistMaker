package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModules
import com.example.playlistmaker.di.domainModules
import com.example.playlistmaker.di.viewModelModules
import com.example.playlistmaker.settings.data.SettingsStorage
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(dataModules, domainModules, viewModelModules)
        }
        resourcess = resources

        PermissionRequester.initialize(applicationContext)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val settingsStorage = SettingsStorage(sharedPreferences)

        if (sharedPreferences.contains(NIGHT_MODE_KEY)) {
            changeTheme(sharedPreferences.getBoolean(NIGHT_MODE_KEY, false))
        } else {
            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {

                Configuration.UI_MODE_NIGHT_YES -> {
                    settingsStorage.updateThemeSetting(true)
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    settingsStorage.updateThemeSetting(false)
                }

                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    settingsStorage.updateThemeSetting(false)
                }
            }
        }
    }

    companion object {
        const val SHARED_PREFERENCES = "sharedPreferences"
        const val NIGHT_MODE_KEY = "nightMode"
        private lateinit var resourcess: Resources

        fun changeTheme(isNightMode: Boolean) {
            when (isNightMode) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        fun getAppResources(): Resources {
            return resourcess
        }
    }
}