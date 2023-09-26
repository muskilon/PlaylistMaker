package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaLibraryButton = findViewById<Button>(R.id.medialibrary_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

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