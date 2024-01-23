package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.medialibrary.ui.MedialibraryActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.searchButton.setOnClickListener {
            val openSearch = Intent(this, SearchActivity::class.java)
            startActivity(openSearch)
        }

        binding.mediaLibraryButton.setOnClickListener {
            val openMediaLibrary = Intent(this, MedialibraryActivity::class.java)
            startActivity(openMediaLibrary)
        }

        binding.settingsButton.setOnClickListener {
            val openSettings = Intent(this, SettingsActivity::class.java)
            startActivity(openSettings)
        }
    }
}