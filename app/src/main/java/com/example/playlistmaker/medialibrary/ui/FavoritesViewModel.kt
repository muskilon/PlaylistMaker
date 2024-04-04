package com.example.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.FavoritesInteractor
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {
    private val liveSongs = MutableLiveData<List<Track>>()

    fun getFavorites() {
        viewModelScope.launch {
            favoritesInteractor.updateFavorites()
            liveSongs.postValue(favoritesInteractor.getFavorites())
        }
    }

    fun getSongs(): LiveData<List<Track>> = liveSongs

    fun onTrackClick(track: Track) {
        tracksInteractor.setCurrentTrack(track)
    }
}