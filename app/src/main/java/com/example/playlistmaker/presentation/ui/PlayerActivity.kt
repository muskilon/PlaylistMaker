package com.example.playlistmaker.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.MusicPlayerImpl
import com.example.playlistmaker.data.OnStateChangeListener
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.PlayerState
import com.example.playlistmaker.domain.Track
import kotlinx.coroutines.Runnable

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    //private lateinit var viewModel: PlayerViewModel
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    private var handler: Handler? = null
    val mplayer = MusicPlayerImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handler = Handler(Looper.getMainLooper())
        val currentTrack =
            IntentCompat.getParcelableExtra(intent, CURRENT_TRACK, Track::class.java) ?: Track()
        //viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(currentTrack))[PlayerViewModel::class.java]

        mplayer.preparePlayer(currentTrack.previewUrl)
        mplayer.setListener(
            object : OnStateChangeListener {
                override fun onChange(state: PlayerState) {
                    when (state) {
                        PlayerState.STATE_PREPARED -> {
                            binding.playButton.isClickable = true
                            binding.timeElapsed.text = TIMER_ZERO
                            playerState = PlayerState.STATE_PREPARED
                        }

                        PlayerState.STATE_END_OF_SONG -> {
                            binding.playButton.setImageResource(R.drawable.play_button)
                            binding.timeElapsed.text = TIMER_ZERO
                            playerState = PlayerState.STATE_PREPARED
                        }

                        else -> {
                            //nothing
                        }
                    }
                }
            }
        )

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
            PlayerState.STATE_PLAYING -> {
                mplayer.pause()
                binding.playButton.setImageResource(R.drawable.play_button)
                playerState = PlayerState.STATE_PAUSED
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                mplayer.start()
                binding.playButton.setImageResource(R.drawable.pause_button)
                playerState = PlayerState.STATE_PLAYING
                startTimer()
            }

            else -> {
                //nothing
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
                    PlayerState.STATE_PLAYING -> {
                        binding.timeElapsed.text = mplayer.getCurrentPosition()
                        handler?.postDelayed(this, TIMER_PERIOD_UPDATE)
                    }

                    PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT, PlayerState.STATE_END_OF_SONG -> handler?.removeCallbacks(
                        this
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mplayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun onDestroy() {
        super.onDestroy()
        mplayer.stop()
    }

    companion object {
        const val CURRENT_TRACK = "currentTrack"
        const val TIMER_ZERO = "00:00"
        const val TIMER_PERIOD_UPDATE = 300L
    }
}