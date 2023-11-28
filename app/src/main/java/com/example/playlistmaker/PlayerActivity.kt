package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import kotlinx.coroutines.Runnable
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var currentTrack: Track? = null
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handler = Handler(Looper.getMainLooper())
        currentTrack = IntentCompat.getParcelableExtra(intent, CURRENT_TRACK, Track::class.java)
        preparePlayer()

        binding.arrowBack.setOnClickListener {
            this.finish()
        }

        Glide.with(this)
            .load(currentTrack?.artworkUrl512)
            .placeholder(R.drawable.placeholder_big)
            .transform(
                RoundedCorners(
                    view.resources.getDimension(R.dimen.player_album_cover_corner_radius).toInt()
                )
            )
            .into(binding.albumCover)
        binding.trackName.text = currentTrack?.trackName
        binding.artistName.text = currentTrack?.artistName
        binding.trackDuration.text = currentTrack?.trackTime
        binding.album.text = currentTrack?.collectionName
        binding.year.text = currentTrack?.year
        binding.genre.text = currentTrack?.primaryGenreName
        binding.country.text = currentTrack?.country

        binding.playButton.setOnClickListener { playbackControl() }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(currentTrack?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isClickable = true
            binding.timeElapsed.text = START_TIMER
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.play_button)
            binding.timeElapsed.text = START_TIMER
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        binding.playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        handler?.post(
            timerTask()
        )
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun timerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        binding.timeElapsed.text = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition).toString()
                        handler?.postDelayed(this, TIMER_PERIOD_UPDATE)
                    }

                    STATE_PREPARED, STATE_PAUSED, STATE_DEFAULT -> handler?.removeCallbacks(this)
                }
            }
        }
    }

    companion object {
        const val CURRENT_TRACK = "currentTrack"
        const val START_TIMER = "00:00"
        const val TIMER_PERIOD_UPDATE = 300L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}