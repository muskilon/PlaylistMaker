package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            viewModel.setListener()
            viewModel.preparePlayer()
        }

        viewModel.getPlayStatus().observe(this) { status ->
            binding.timeElapsed.text = status.timeElapsed
            binding.playButton.isClickable = status.playButtonClickableState
            binding.playButton.setImageResource(PlayerButtons.valueOf(status.playButtonImage).button)

            Glide.with(this)
                .load(status.currentTrack.pictureUrl)
                .placeholder(R.drawable.placeholder_big)
                .transform(
                    RoundedCorners(
                        view.resources.getDimension(R.dimen.player_album_cover_corner_radius)
                            .toInt()
                    )
                )
                .into(binding.albumCover)
            binding.trackName.text = status.currentTrack.trackName
            binding.artistName.text = status.currentTrack.artist
            binding.trackDuration.text = status.currentTrack.duration
            binding.album.text = status.currentTrack.album
            binding.year.text = status.currentTrack.year
            binding.genre.text = status.currentTrack.genre
            binding.country.text = status.currentTrack.country
        }

        binding.playButton.setOnClickListener { viewModel.playbackControl() }

        binding.addToFavorites.setOnClickListener {
            viewModel.addToFavorites()
        }

        onBackPressedDispatcher.addCallback(this, callback)
        binding.arrowBack.setOnClickListener {
            exit()
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exit()
        }
    }

    fun exit() {
        viewModel.stopPlayer()
        this.finish()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        enum class PlayerButtons(val button: Int) {
            PLAY(R.drawable.play_button),
            PAUSE(R.drawable.pause_button)
        }
    }
}