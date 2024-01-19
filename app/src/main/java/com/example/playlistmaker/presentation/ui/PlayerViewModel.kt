package com.example.playlistmaker.presentation.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.data.MusicPlayerImpl
import com.example.playlistmaker.data.OnStateChangeListener
import com.example.playlistmaker.domain.PlayerState
import com.example.playlistmaker.domain.TrackModel
import com.example.playlistmaker.domain.TrackModelInteractor

class PlayerViewModel : ViewModel() {
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    private val mplayer = MusicPlayerImpl()
    private val currentTrack = TrackModelInteractor.getTrackModel()
    private val liveCurrentTrack = MutableLiveData(currentTrack)
    private val handler = Handler(Looper.getMainLooper())
    private var liveTimeElapsed = MutableLiveData<String>()
    private var livePlayButtonClickableState = MutableLiveData<Boolean>()
    private var livePlayButtonImage = MutableLiveData<Int>()

    fun setListener() {
        mplayer.setListener(
            object : OnStateChangeListener {
                override fun onChange(state: PlayerState) {
                    when (state) {
                        PlayerState.STATE_PREPARED -> {
                            livePlayButtonClickableState.value = true
                            liveTimeElapsed.value = TIMER_ZERO
                            playerState = PlayerState.STATE_PREPARED
                        }

                        PlayerState.STATE_END_OF_SONG -> {
                            livePlayButtonImage = MutableLiveData(R.drawable.play_button)
                            liveTimeElapsed.value = TIMER_ZERO
                            playerState = PlayerState.STATE_PREPARED
                        }

                        else -> {
                            //nothing
                        }
                    }
                }
            }
        )

    }

    fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                mplayer.pause()
                livePlayButtonImage.value = R.drawable.play_button
                playerState = PlayerState.STATE_PAUSED
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                mplayer.start()
                livePlayButtonImage.value = R.drawable.pause_button
                playerState = PlayerState.STATE_PLAYING
                startTimer()
            }

            else -> {
                //nothing
            }
        }
    }

    private fun startTimer() {
        handler.post(
            timerTask()
        )
    }

    private fun timerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    PlayerState.STATE_PLAYING -> {
                        liveTimeElapsed.value = mplayer.getCurrentPosition()
                        Log.d("TAG", mplayer.getCurrentPosition())
                        handler.postDelayed(this, TIMER_PERIOD_UPDATE)
                    }

                    PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT, PlayerState.STATE_END_OF_SONG -> handler.removeCallbacks(
                        this
                    )
                }
            }
        }
    }

    fun getPlayButtonImage(): LiveData<Int> {
        return livePlayButtonImage
    }

    fun getPlayButtonState(): LiveData<Boolean> {
        return livePlayButtonClickableState
    }

    fun getTimeElapsed(): LiveData<String> {
        return liveTimeElapsed
    }

    fun getCurrentTrack(): LiveData<TrackModel> {
        return liveCurrentTrack
    }

    fun preparePlayer() {
        mplayer.preparePlayer(currentTrack.previewUrl)
    }

    fun pausePlayer() {
        mplayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    fun stopPlayer() {
        mplayer.stop()
    }

    companion object {
        const val TIMER_ZERO = "00:00"
        const val TIMER_PERIOD_UPDATE = 300L
    }

}