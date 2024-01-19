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

    //private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    //private var handler: Handler? = null
    //private val currentTrack = TrackModelInteractor.getTrackModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //handler = Handler(Looper.getMainLooper())
        viewModel = ViewModelProvider(this, PlayerViewModelFactory())[PlayerViewModel::class.java]

        viewModel.getTimeElapsed().observe(this) { timeElapsed ->
            binding.timeElapsed.text = timeElapsed
        }

        viewModel.getPlayButtonState().observe(this) { playButtonState ->
            binding.playButton.isClickable = playButtonState
        }

        viewModel.getPlayButtonImage().observe(this) { playButtonImage ->
            binding.playButton.setImageResource(playButtonImage)
        }


        viewModel.preparePlayer()
        viewModel.setListener()
//        viewModel.mplayer.setListener(
//            object : OnStateChangeListener {
//                override fun onChange(state: PlayerState) {
//                    when (state) {
//                        PlayerState.STATE_PREPARED -> {
//                            binding.playButton.isClickable = true
//                            binding.timeElapsed.text = TIMER_ZERO
//                            playerState = PlayerState.STATE_PREPARED
//                        }
//
//                        PlayerState.STATE_END_OF_SONG -> {
//                            binding.playButton.setImageResource(R.drawable.play_button)
//                            binding.timeElapsed.text = TIMER_ZERO
//                            playerState = PlayerState.STATE_PREPARED
//                        }
//
//                        else -> {
//                            //nothing
//                        }
//                    }
//                }
//            }
//        )

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

//    private fun playbackControl() {
//        when (playerState) {
//            PlayerState.STATE_PLAYING -> {
//                viewModel.mplayer.pause()
//                binding.playButton.setImageResource(R.drawable.play_button)
//                playerState = PlayerState.STATE_PAUSED
//            }
//
//            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
//                viewModel.mplayer.start()
//                binding.playButton.setImageResource(R.drawable.pause_button)
//                playerState = PlayerState.STATE_PLAYING
//                startTimer()
//            }
//
//            else -> {
//                //nothing
//            }
//        }
//    }

//    private fun startTimer() {
//        handler?.post(
//            timerTask()
//        )
//    }

//    private fun timerTask(): Runnable {
//        return object : Runnable {
//            override fun run() {
//                when (playerState) {
//                    PlayerState.STATE_PLAYING -> {
//                        binding.timeElapsed.text = viewModel.getCurrentPosition()
//                        handler?.postDelayed(this, TIMER_PERIOD_UPDATE)
//                    }
//
//                    PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT, PlayerState.STATE_END_OF_SONG -> handler?.removeCallbacks(
//                        this
//                    )
//                }
//            }
//        }
//    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPlayer()
    }
}