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
    private val songs = ArrayList<Track>()
    private val liveSongs = MutableLiveData<List<Track>>()
    fun getFavorites() {
        viewModelScope.launch {
            favoritesInteractor.getFavoritesSongs()
                .collect { result -> processResult(result) }
        }
    }

    fun getSongs(): LiveData<List<Track>> = liveSongs

    private fun processResult(favorites: List<Track>) {
        songs.clear()
        songs.addAll(favorites)
        liveSongs.postValue(songs)
    }

    fun onTrackClick(track: Track) {
//        track.isFavorites = true
        tracksInteractor.setCurrentTrack(track)
    }
}