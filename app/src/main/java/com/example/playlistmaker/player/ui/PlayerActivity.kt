package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()
    private var isClickAllowed = true
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
            if (status.isFavorites) {
                binding.addToFavorites.setImageResource(R.drawable.favorite_button_on)
            } else {
                binding.addToFavorites.setImageResource(R.drawable.favorite_button_off)
            }

            Glide.with(this)
                .load(status.currentTrack.artworkUrl512)
                .placeholder(R.drawable.placeholder_big)
                .transform(
                    RoundedCorners(
                        view.resources.getDimension(R.dimen.player_album_cover_corner_radius)
                            .toInt()
                    )
                )
                .into(binding.albumCover)
            binding.trackName.text = status.currentTrack.trackName
            binding.artistName.text = status.currentTrack.artistName
            binding.trackDuration.text = status.currentTrack.trackTime
            binding.album.text = status.currentTrack.collectionName
            binding.year.text = status.currentTrack.year
            binding.genre.text = status.currentTrack.primaryGenreName
            binding.country.text = status.currentTrack.country
        }

        binding.playButton.setOnClickListener { viewModel.playbackControl() }

        binding.addToFavorites.setOnClickListener {
            if (clickDebounce()) viewModel.addToFavorites()
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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
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
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        enum class PlayerButtons(val button: Int) {
            PLAY(R.drawable.play_button),
            PAUSE(R.drawable.pause_button)
        }
    }
}