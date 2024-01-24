package com.example.playlistmaker.player.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.data.MusicPlayerImpl

class PlayerViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {
    private val tracksInteractor = Creator.provideTracksInteractor(context)
    private val mplayer = MusicPlayerImpl()
    private val handler = Handler(Looper.getMainLooper())
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            tracksInteractor = tracksInteractor,
            mplayer = mplayer,
            handler = handler
        ) as T
    }
}