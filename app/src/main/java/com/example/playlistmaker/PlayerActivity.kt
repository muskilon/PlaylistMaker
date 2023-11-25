package com.example.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val currentTrack = gson.fromJson(intent.getStringExtra("currentTrack"), Track::class.java)
        val cornerRadius = view.resources.getDimension(R.dimen.player_album_cover_corner_radius)

        binding.arrowBack.setOnClickListener{
            this.finish()
        }
        Glide.with(this)
            .load(currentTrack.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadius.toInt()))
            .into(binding.albumCover)
        binding.trackName.text = currentTrack.trackName
        binding.artistName.text = currentTrack.artistName
        binding.trackDuration.text = currentTrack.trackTime
        binding.album.text = currentTrack.collectionName
        binding.year.text = currentTrack.year
        binding.genre.text = currentTrack.primaryGenreName
        binding.country.text = currentTrack.country
    }
}