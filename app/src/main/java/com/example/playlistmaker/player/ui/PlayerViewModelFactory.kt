package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.data.MusicPlayerImpl

@Suppress("UNCHECKED_CAST")
class PlayerViewModelFactory : ViewModelProvider.Factory {
    private val currentTrackInteractor = Creator.provideCurrentTrackInteractor()
    private val mplayer = MusicPlayerImpl()
    private val handler = Handler(Looper.getMainLooper())
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            currentTrackInteractor = currentTrackInteractor,
            mplayer = mplayer,
            handler = handler
        ) as T
    }
}