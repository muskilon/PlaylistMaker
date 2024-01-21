package com.example.playlistmaker.presentation.ui

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.data.MusicPlayerImpl
import com.example.playlistmaker.data.OnStateChangeListener
import com.example.playlistmaker.domain.MusicPlayerState
import com.example.playlistmaker.domain.TrackModel

class PlayerViewModel(
    private val currentTrack: TrackModel,
    private val mplayer: MusicPlayerImpl,
    private val handler: Handler
) : ViewModel() {
    private var musicPlayerState = MusicPlayerState.STATE_DEFAULT
    private val liveCurrentTrack = MutableLiveData(currentTrack)
    private var liveTimeElapsed = MutableLiveData<String>()
    private var livePlayButtonClickableState = MutableLiveData<Boolean>()
    private var livePlayButtonImage = MutableLiveData<Int>()

    fun setListener() {
        mplayer.setListener(
            object : OnStateChangeListener {
                override fun onChange(state: MusicPlayerState) {
                    if (state == MusicPlayerState.STATE_PREPARED) {
                        livePlayButtonClickableState.postValue(true)
                        liveTimeElapsed.postValue(TIMER_ZERO)
                        musicPlayerState = MusicPlayerState.STATE_PREPARED
                    }

                    if (state == MusicPlayerState.STATE_END_OF_SONG) {
                        livePlayButtonImage.postValue(R.drawable.play_button)
                        liveTimeElapsed.postValue(TIMER_ZERO)
                        musicPlayerState = MusicPlayerState.STATE_PREPARED
                    }

                }
            }
        )

    }

    fun playbackControl() {

        when (musicPlayerState) {
            MusicPlayerState.STATE_PLAYING -> {
                mplayer.pause()
                livePlayButtonImage.postValue(R.drawable.play_button)
                musicPlayerState = MusicPlayerState.STATE_PAUSED
            }

            MusicPlayerState.STATE_PAUSED, MusicPlayerState.STATE_PREPARED -> {
                mplayer.start()
                livePlayButtonImage.postValue(R.drawable.pause_button)
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
                        liveTimeElapsed.postValue(mplayer.getCurrentPosition())
//                        Log.d("TAG", mplayer.getCurrentPosition())
//                        Log.d("TAG", musicPlayerState.name)
                        handler.postDelayed(this, TIMER_PERIOD_UPDATE)
                    }

                    MusicPlayerState.STATE_PREPARED, MusicPlayerState.STATE_PAUSED, MusicPlayerState.STATE_DEFAULT, MusicPlayerState.STATE_END_OF_SONG -> handler.removeCallbacks(
                        this
                    )
                }
            }
        }
    }

    fun getPlayButtonImage(): LiveData<Int> = livePlayButtonImage

    fun getPlayButtonState(): LiveData<Boolean> = livePlayButtonClickableState

    fun getTimeElapsed(): LiveData<String> = liveTimeElapsed

    fun getCurrentTrack(): LiveData<TrackModel> = liveCurrentTrack

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