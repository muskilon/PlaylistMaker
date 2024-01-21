package com.example.playlistmaker.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, PlayerViewModelFactory())[PlayerViewModel::class.java]

        viewModel.setListener()
        viewModel.preparePlayer()

        viewModel.getTimeElapsed().observe(this) { timeElapsed ->
            binding.timeElapsed.text = timeElapsed
        }

        viewModel.getPlayButtonState().observe(this) { playButtonState ->
            binding.playButton.isClickable = playButtonState
        }

        viewModel.getPlayButtonImage().observe(this) { playButtonImage ->
            binding.playButton.setImageResource(playButtonImage)
        }

        binding.arrowBack.setOnClickListener {
            this.finish()
        }

        viewModel.getCurrentTrack().observe(this) { currentTrack ->
            Glide.with(this)
                .load(currentTrack.pictureUrl)
                .placeholder(R.drawable.placeholder_big)
                .transform(
                    RoundedCorners(
                        view.resources.getDimension(R.dimen.player_album_cover_corner_radius)
                            .toInt()
                    )
                )
                .into(binding.albumCover)
            binding.trackName.text = currentTrack.trackName
            binding.artistName.text = currentTrack.artist
            binding.trackDuration.text = currentTrack.duration
            binding.album.text = currentTrack.album
            binding.year.text = currentTrack.year
            binding.genre.text = currentTrack.genre
            binding.country.text = currentTrack.country
        }

        binding.playButton.setOnClickListener { viewModel.playbackControl() }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPlayer()
    }
}