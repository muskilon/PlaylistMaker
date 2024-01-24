package com.example.playlistmaker.player.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.MusicPlayerImpl
import com.example.playlistmaker.player.data.OnStateChangeListener
import com.example.playlistmaker.player.domain.MusicPlayerState
import com.example.playlistmaker.player.domain.PlayStatus
import com.example.playlistmaker.player.domain.TrackModel

class PlayerViewModel(
    private val currentTrack: TrackModel,
    private val mplayer: MusicPlayerImpl,
    private val handler: Handler
) : ViewModel() {
    private var musicPlayerState = MusicPlayerState.STATE_DEFAULT

    private var livePlayStatus = MutableLiveData<PlayStatus>()

    init {
        livePlayStatus.value = PlayStatus(
            timeElapsed = "__:__",
            playButtonClickableState = false,
            playButtonImage = (R.drawable.play_button),
            currentTrack = currentTrack
        )
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return livePlayStatus.value ?: PlayStatus(
            timeElapsed = TIMER_ZERO,
            playButtonClickableState = false,
            playButtonImage = (R.drawable.play_button),
            currentTrack = currentTrack
        )
    }

    fun setListener() {
        mplayer.setListener(object : OnStateChangeListener {
            override fun onChange(state: MusicPlayerState) {
                if (state == MusicPlayerState.STATE_PREPARED) {
                    livePlayStatus.value = getCurrentPlayStatus().copy(
                        playButtonClickableState = true,
                        timeElapsed = TIMER_ZERO
                    )
                    musicPlayerState = MusicPlayerState.STATE_PREPARED
                }

                if (state == MusicPlayerState.STATE_END_OF_SONG) {
                    livePlayStatus.value = getCurrentPlayStatus().copy(
                        playButtonImage = (R.drawable.play_button),
                        timeElapsed = TIMER_ZERO
                    )
                    musicPlayerState = MusicPlayerState.STATE_PREPARED
                }

            }
        })

    }

    fun playbackControl() {

        when (musicPlayerState) {
            MusicPlayerState.STATE_PLAYING -> {
                mplayer.pause()
                livePlayStatus.value =
                    getCurrentPlayStatus().copy(playButtonImage = (R.drawable.play_button))
                musicPlayerState = MusicPlayerState.STATE_PAUSED
            }

            MusicPlayerState.STATE_PAUSED, MusicPlayerState.STATE_PREPARED -> {
                mplayer.start()
                livePlayStatus.value =
                    getCurrentPlayStatus().copy(playButtonImage = (R.drawable.pause_button))
                musicPlayerState = MusicPlayerState.STATE_PLAYING
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
                when (musicPlayerState) {
                    MusicPlayerState.STATE_PLAYING -> {
                        livePlayStatus.value =
                            getCurrentPlayStatus().copy(timeElapsed = mplayer.getCurrentPosition())
                        handler.postDelayed(this, TIMER_PERIOD_UPDATE)
                    }

                    MusicPlayerState.STATE_PREPARED, MusicPlayerState.STATE_PAUSED, MusicPlayerState.STATE_DEFAULT, MusicPlayerState.STATE_END_OF_SONG -> handler.removeCallbacks(
                        this
                    )
                }
            }
        }
    }

    fun getPlayStatus(): LiveData<PlayStatus> = livePlayStatus

    fun preparePlayer() {
        mplayer.preparePlayer(currentTrack.previewUrl)
    }

    fun pausePlayer() {
        mplayer.pause()
        musicPlayerState = MusicPlayerState.STATE_PAUSED
    }

    fun stopPlayer() {
        mplayer.stop()
    }

    companion object {
        const val TIMER_ZERO = "00:00"
        const val TIMER_PERIOD_UPDATE = 300L
    }

}