package com.example.playlistmaker.medialibrary.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playListsInteractor: PlayListInteractor,
    private val filesInteractor: FilesInteractor
) : ViewModel(
) {
    private val livePlayLists = MutableLiveData<List<PlayList>>()

    fun updatePlayLists() {
        viewModelScope.launch {
            playListsInteractor.updatePlayLists()
            livePlayLists.postValue(playListsInteractor.getPlayLists())
        }
    }

    fun deletePlayList(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            playListsInteractor.deletePlayList(playList)
            if (playList.cover != null) {
                filesInteractor.deletePlayListCover(playList.cover)
            }
            updatePlayLists()
        }
    }

    fun getPlayLists(): LiveData<List<PlayList>> = livePlayLists
    fun onPlayListClick(playList: PlayList) {
        Log.d("TAG", "Отправляем на список треков плейлиста")
    }
}