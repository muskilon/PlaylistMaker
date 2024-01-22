package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        init(context = this)
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