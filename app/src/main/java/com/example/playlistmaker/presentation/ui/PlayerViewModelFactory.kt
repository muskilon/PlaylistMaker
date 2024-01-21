package com.example.playlistmaker.presentation.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.data.MusicPlayerImpl
import com.example.playlistmaker.domain.TrackModelInteractor

class PlayerViewModelFactory : ViewModelProvider.Factory {
    private val currentTrack = TrackModelInteractor.getTrackModel()
    private val mplayer = MusicPlayerImpl()
    private val handler = Handler(Looper.getMainLooper())
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            currentTrack = currentTrack,
            mplayer = mplayer,
            handler = handler
        ) as T
    }
}