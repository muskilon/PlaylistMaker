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
    private val favoritesInterActor: FavoritesInteractor,
    private val tracksInterActor: TracksInteractor
) : ViewModel() {
    private val liveSongs = MutableLiveData<List<Track>>()

    fun getFavorites() {
        viewModelScope.launch {
            favoritesInterActor.updateFavorites()
            if (favoritesInterActor.getFavorites() != liveSongs.value) {
                liveSongs.postValue(favoritesInterActor.getFavorites())
            }
        }
    }

    fun getSongs(): LiveData<List<Track>> = liveSongs

    fun onTrackClick(track: Track) {
        tracksInterActor.setCurrentTrack(track)
    }
}