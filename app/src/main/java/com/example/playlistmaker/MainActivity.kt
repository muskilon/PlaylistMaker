package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES = "sharedPreferences"
const val NIGHT_MODE_KEY = "nightMode"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaLibraryButton = findViewById<Button>(R.id.medialibrary_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        if (sharedPreferences.contains(NIGHT_MODE_KEY)) {
            when(sharedPreferences.getBoolean(NIGHT_MODE_KEY, false)) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        searchButton.setOnClickListener {
            val openSearch = Intent(this, SearchActivity::class.java)
            startActivity(openSearch)
        }

        mediaLibraryButton.setOnClickListener {
            val openMediaLibrary = Intent(this, MedialibraryActivity::class.java)
            startActivity(openMediaLibrary)
        }

        settingsButton.setOnClickListener {
            val openSettings = Intent(this, SettingsActivity::class.java)
            startActivity(openSettings)
        }
    }
}