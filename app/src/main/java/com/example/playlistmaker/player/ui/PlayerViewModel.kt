package com.example.playlistmaker.player.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.MusicPlayer
import com.example.playlistmaker.player.data.OnStateChangeListener
import com.example.playlistmaker.player.domain.CurrentTrackInteractor
import com.example.playlistmaker.player.domain.MusicPlayerState
import com.example.playlistmaker.player.domain.PlayStatus

class PlayerViewModel(
    currentTrackInteractor: CurrentTrackInteractor,
    private val mplayer: MusicPlayer,
    private val handler: Handler
) : ViewModel() {
    private val timerZero = MyApplication.getAppResources().getString(R.string.timer_zero)
    private val timerPlaceholder =
        MyApplication.getAppResources().getString(R.string.timer_placeholder)
    private var musicPlayerState = MusicPlayerState.STATE_DEFAULT
    private var livePlayStatus = MutableLiveData<PlayStatus>()
    private val currentTrack = currentTrackInteractor.getCurrentTrack()

    init {
        livePlayStatus.value = PlayStatus(
            timeElapsed = timerPlaceholder,
            playButtonClickableState = false,
            playButtonImage = PLAY,
            currentTrack = currentTrack
        )
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return livePlayStatus.value ?: PlayStatus(
            timeElapsed = timerZero,
            playButtonClickableState = false,
            playButtonImage = PLAY,
            currentTrack = currentTrack
        )
    }

    fun setListener() {
        mplayer.setListener(object : OnStateChangeListener {
            override fun onChange(state: MusicPlayerState) {
                if (state == MusicPlayerState.STATE_PREPARED) {
                    livePlayStatus.value = getCurrentPlayStatus().copy(
                        playButtonClickableState = true,
                        timeElapsed = timerZero
                    )
                    musicPlayerState = MusicPlayerState.STATE_PREPARED
                }

                if (state == MusicPlayerState.STATE_END_OF_SONG) {
                    livePlayStatus.value = getCurrentPlayStatus().copy(
                        playButtonImage = PLAY,
                        timeElapsed = timerZero
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
                    getCurrentPlayStatus().copy(playButtonImage = PLAY)
                musicPlayerState = MusicPlayerState.STATE_PAUSED
            }

            MusicPlayerState.STATE_PAUSED, MusicPlayerState.STATE_PREPARED -> {
                mplayer.start()
                livePlayStatus.value =
                    getCurrentPlayStatus().copy(playButtonImage = PAUSE)
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
        if (musicPlayerState == MusicPlayerState.STATE_PLAYING) {
            mplayer.pause()
            livePlayStatus.value =
                getCurrentPlayStatus().copy(playButtonImage = PLAY)
            musicPlayerState = MusicPlayerState.STATE_PAUSED
        }
    }

    fun stopPlayer() {
        pausePlayer()
        mplayer.stop()
    }

    companion object {
        private const val TIMER_PERIOD_UPDATE = 300L
        private const val PLAY = "PLAY"
        private const val PAUSE = "PAUSE"
    }

}