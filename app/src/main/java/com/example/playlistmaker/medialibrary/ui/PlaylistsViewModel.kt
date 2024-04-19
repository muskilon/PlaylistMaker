package com.example.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playListsInteractor: PlayListInteractor,
) : ViewModel(
) {
    private val livePlayLists = MutableLiveData<List<PlayList>>()

    fun updatePlayLists() {
        viewModelScope.launch {
            playListsInteractor.updatePlayLists()
            livePlayLists.postValue(playListsInteractor.getPlayLists())
        }
    }

    fun getPlayLists(): LiveData<List<PlayList>> = livePlayLists
}