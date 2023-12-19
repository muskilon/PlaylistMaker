package com.example.playlistmaker.presentation.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class MedialibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medialibrary)
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            this.finish()
        }

    }
}