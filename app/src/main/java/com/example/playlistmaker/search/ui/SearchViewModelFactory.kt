package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SearchViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {
    private val tracksInteractor = Creator.provideTracksInteractor(context)
    private val handler = Handler(Looper.getMainLooper())

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            tracksInteractor = tracksInteractor,
            handler = handler
        ) as T
    }
}