package com.example.playlistmaker.presentation.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.data.MusicPlayerImpl
import com.example.playlistmaker.domain.PlayerState
import com.example.playlistmaker.domain.TrackModelInteractor

class PlayerViewModel : ViewModel() {
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    val mplayer = MusicPlayerImpl()
    private val currentTrack = TrackModelInteractor.getTrackModel()

}