package com.example.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInterActor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playListsInterActor: PlayListInterActor,
) : ViewModel(
) {
    private val livePlayLists = MutableLiveData<List<PlayList>>()

    fun updatePlayLists() {
        viewModelScope.launch {
            playListsInterActor.updatePlayLists()
            livePlayLists.postValue(playListsInterActor.getPlayLists())
        }
    }

    fun getPlayLists(): LiveData<List<PlayList>> = livePlayLists
}