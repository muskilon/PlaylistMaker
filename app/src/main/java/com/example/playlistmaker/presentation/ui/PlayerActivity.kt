package com.example.playlistmaker.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.impl.MediaPlayerImpl
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var playerState = STATE_DEFAULT
    private var handler: Handler? = null
    val mplayer = MediaPlayerImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handler = Handler(Looper.getMainLooper())
        val currentTrack =
            IntentCompat.getParcelableExtra(intent, CURRENT_TRACK, Track::class.java) ?: Track()

        mplayer.preparePlayer(currentTrack.previewUrl)

        mplayer.mediaPlayer.setOnPreparedListener {
            binding.playButton.isClickable = true
            binding.timeElapsed.text = TIMER_ZERO
            playerState = STATE_PREPARED
        }
        mplayer.mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.play_button)
            binding.timeElapsed.text = TIMER_ZERO
            playerState = STATE_PREPARED
        }

        binding.arrowBack.setOnClickListener {
            this.finish()
        }

        Glide.with(this)
            .load(currentTrack.artworkUrl512)
            .placeholder(R.drawable.placeholder_big)
            .transform(
                RoundedCorners(
                    view.resources.getDimension(R.dimen.player_album_cover_corner_radius).toInt()
                )
            )
            .into(binding.albumCover)
        binding.trackName.text = currentTrack.trackName
        binding.artistName.text = currentTrack.artistName
        binding.trackDuration.text = currentTrack.trackTime
        binding.album.text = currentTrack.collectionName
        binding.year.text = currentTrack.year
        binding.genre.text = currentTrack.primaryGenreName
        binding.country.text = currentTrack.country

        binding.playButton.setOnClickListener { playbackControl() }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                mplayer.pause()
                binding.playButton.setImageResource(R.drawable.play_button)
                playerState = STATE_PAUSED
            }

            STATE_PREPARED, STATE_PAUSED -> {
                mplayer.start()
                binding.playButton.setImageResource(R.drawable.pause_button)
                playerState = STATE_PLAYING
                startTimer()
            }
        }
    }

    private fun startTimer() {
        handler?.post(
            timerTask()
        )
    }

    private fun timerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        Log.d("TAG", binding.timeElapsed.text.toString())
                        binding.timeElapsed.text = mplayer.getCurrentPosition()
                        handler?.postDelayed(this, TIMER_PERIOD_UPDATE)
                    }
                    STATE_PREPARED, STATE_PAUSED, STATE_DEFAULT -> handler?.removeCallbacks(this)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mplayer.pause()
        playerState = STATE_PAUSED
    }

    override fun onDestroy() {
        super.onDestroy()
        mplayer.stop()
    }

    companion object {
        const val CURRENT_TRACK = "currentTrack"
        const val TIMER_ZERO = "00:00"
        const val TIMER_PERIOD_UPDATE = 300L
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}