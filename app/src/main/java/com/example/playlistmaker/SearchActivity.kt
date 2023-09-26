package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backArrow = findViewById<ImageView>(R.id.arrow_back)
        backArrow.setOnClickListener {
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)
        }
    }
}