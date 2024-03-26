package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.MusicPlayer
import com.example.playlistmaker.player.data.OnStateChangeListener
import com.example.playlistmaker.player.domain.CurrentTrackInteractor
import com.example.playlistmaker.player.domain.FavoritesInteractor
import com.example.playlistmaker.player.domain.MusicPlayerState
import com.example.playlistmaker.player.domain.PlayStatus
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    currentTrackInteractor: CurrentTrackInteractor,
    private val trackInteractor: TracksInteractor,
    private val mplayer: MusicPlayer,
) : ViewModel() {
    private val timerZero = MyApplication.getAppResources().getString(R.string.timer_zero)
    private val timerPlaceholder =
        MyApplication.getAppResources().getString(R.string.timer_placeholder)
    private var musicPlayerState = MusicPlayerState.STATE_DEFAULT
    private var livePlayStatus = MutableLiveData<PlayStatus>()
    private val currentTrack = currentTrackInteractor.getCurrentTrack()
    private var isUserPaused: Boolean = false

    init {
        livePlayStatus.value = PlayStatus(
            timeElapsed = timerPlaceholder,
            playButtonClickableState = false,
            playButtonImage = PLAY,
            currentTrack = currentTrack,
            isFavorites = currentTrack.isFavorites
        )
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return livePlayStatus.value ?: PlayStatus(
            timeElapsed = timerZero,
            playButtonClickableState = false,
            playButtonImage = PLAY,
            currentTrack = currentTrack,
            isFavorites = currentTrack.isFavorites
        )
    }

    fun setListener() {
        mplayer.setListener(object : OnStateChangeListener {
            override fun onChange(state: MusicPlayerState) {
                if (state == MusicPlayerState.STATE_PREPARED) {
                    livePlayStatus.value = getCurrentPlayStatus().copy(
                        playButtonClickableState = true, timeElapsed = timerZero
                    )
                    musicPlayerState = MusicPlayerState.STATE_PREPARED
                }

                if (state == MusicPlayerState.STATE_END_OF_SONG) {
                    livePlayStatus.value = getCurrentPlayStatus().copy(
                        playButtonImage = PLAY, timeElapsed = timerZero
                    )
                    musicPlayerState = MusicPlayerState.STATE_PREPARED
                }

            }
        })

    }

    fun playbackControl() {
        when (musicPlayerState) {
            MusicPlayerState.STATE_PLAYING -> {
                pausePlayer()
                isUserPaused = true
            }

            MusicPlayerState.STATE_PAUSED, MusicPlayerState.STATE_PREPARED -> {
                isUserPaused = false
                mplayer.start()
                livePlayStatus.value = getCurrentPlayStatus().copy(playButtonImage = PAUSE)
                musicPlayerState = MusicPlayerState.STATE_PLAYING
                timer()
            }

            else -> {
                //nothing
            }
        }
    }

    private fun timer() {
        var timerJob: Job? = null
        timerJob = viewModelScope.launch {
            while (musicPlayerState == MusicPlayerState.STATE_PLAYING) {
                livePlayStatus.value =
                    getCurrentPlayStatus().copy(timeElapsed = mplayer.getCurrentPosition())
                delay(TIMER_PERIOD_UPDATE)
            }
            timerJob?.cancel()
        }
    }

    fun addToFavorites() {
        updateFavoritesDb(currentTrack.isFavorites)
        currentTrack.isFavorites = !currentTrack.isFavorites
        trackInteractor.updateHistoryTrack(currentTrack)
        livePlayStatus.value = getCurrentPlayStatus().copy(isFavorites = currentTrack.isFavorites)
    }

    private fun updateFavoritesDb(isFavorites: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorites) favoritesInteractor.deleteSongFromFavorites(currentTrack)
            else favoritesInteractor.addSongToFavorites(currentTrack)
        }
    }

    fun getPlayStatus(): LiveData<PlayStatus> = livePlayStatus

    fun preparePlayer() {
        mplayer.preparePlayer(currentTrack.previewUrl)
    }

    fun pausePlayer() {
        if (musicPlayerState == MusicPlayerState.STATE_PLAYING) {
            mplayer.pause()
            livePlayStatus.value = getCurrentPlayStatus().copy(playButtonImage = PLAY)
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