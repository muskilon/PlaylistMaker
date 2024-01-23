package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.SettingsStorage


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        init(context = this)

        if (sharedPreferences.contains(NIGHT_MODE_KEY)) {
            when (sharedPreferences.getBoolean(NIGHT_MODE_KEY, false)) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else {
            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    sharedPreferences.edit()
                        .putBoolean(SettingsStorage.NIGHT_MODE_KEY, true)
                        .apply()
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    sharedPreferences.edit()
                        .putBoolean(SettingsStorage.NIGHT_MODE_KEY, false)
                        .apply()
                }

                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    sharedPreferences.edit()
                        .putBoolean(SettingsStorage.NIGHT_MODE_KEY, false)
                        .apply()
                }
            }
        }
    }

    companion object SharedPreferencesProvider {
        lateinit var sharedPreferences: SharedPreferences
        const val SHARED_PREFERENCES = "sharedPreferences"
        const val NIGHT_MODE_KEY = "nightMode"

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        }


    }
}