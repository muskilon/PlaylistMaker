package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import com.example.playlistmaker.player.data.MusicPlayer
import com.example.playlistmaker.player.data.OnStateChangeListener
import com.example.playlistmaker.player.domain.CurrentTrackInteractor
import com.example.playlistmaker.player.domain.FavoritesInteractor
import com.example.playlistmaker.player.domain.MusicPlayerState
import com.example.playlistmaker.player.domain.PlayStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val currentTrackInteractor: CurrentTrackInteractor,
    private val playListInteractor: PlayListInteractor,
    private val mplayer: MusicPlayer
) : ViewModel() {
    private val timerZero = MyApplication.getAppResources().getString(R.string.timer_zero)
    private val timerPlaceholder =
        MyApplication.getAppResources().getString(R.string.timer_placeholder)
    private var musicPlayerState = MusicPlayerState.STATE_DEFAULT
    private var livePlayStatus = MutableLiveData<PlayStatus>()
    private var livePlayLists = MutableLiveData<List<PlayList>>()
    private var currentTrack = currentTrackInteractor.getCurrentTrack()
    private var isUserPaused: Boolean = false

    init {
        livePlayStatus.value = PlayStatus(
            timeElapsed = timerPlaceholder,
            playButtonClickableState = false,
            playButtonImage = PLAY,
            currentTrack = currentTrack,
            isFavorites = isFavorites()
        )
    }

    fun updateCurrentTrack() {
        currentTrack = currentTrackInteractor.getCurrentTrack()
        livePlayStatus.value = getCurrentPlayStatus().copy(
            currentTrack = currentTrack,
            isFavorites = isFavorites(),
            timeElapsed = timerPlaceholder,
            playButtonClickableState = false,
            playButtonImage = PLAY
        )
        preparePlayer()
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return livePlayStatus.value ?: PlayStatus(
            timeElapsed = timerZero,
            playButtonClickableState = false,
            playButtonImage = PLAY,
            currentTrack = currentTrack,
            isFavorites = isFavorites()
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
                Unit
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

    private fun isFavorites(): Boolean {
        return favoritesInteractor.getFavorites().contains(currentTrack)
    }

    fun addToFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorites()) favoritesInteractor.deleteSongFromFavorites(currentTrack)
            else favoritesInteractor.addSongToFavorites(currentTrack)
            withContext(Dispatchers.Main) {
                livePlayStatus.value = getCurrentPlayStatus().copy(isFavorites = isFavorites())
            }
        }
    }

    fun updatePlaylists() {
        viewModelScope.launch {
            playListInteractor.updatePlayLists()
            livePlayLists.postValue(playListInteractor.getPlayLists())
        }
    }

    fun getPlayLists(): LiveData<List<PlayList>> = livePlayLists

    fun addTrackToPlayList(playList: PlayList): Boolean {
        return if (playList.tracks.tracks.contains(currentTrack.trackId)) {
            false
        } else {
            viewModelScope.launch {
                playListInteractor.addTrackToPlayList(playList, currentTrack)
            }
            true
        }
    }

    companion object {
        private const val TIMER_PERIOD_UPDATE = 300L
        private const val PLAY = "PLAY"
        private const val PAUSE = "PAUSE"
    }

}