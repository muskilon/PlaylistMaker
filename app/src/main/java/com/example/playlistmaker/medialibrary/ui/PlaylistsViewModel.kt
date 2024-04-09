package com.example.playlistmaker.medialibrary.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import com.example.playlistmaker.player.data.db.PlayListEntity
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playListsInteractor: PlayListInteractor
) : ViewModel(
) {
    private val livePlayLists = MutableLiveData<List<PlayListEntity>>()

    fun updatePlayLists() {
        viewModelScope.launch {
            playListsInteractor.updatePlayLists()
            livePlayLists.postValue(playListsInteractor.getPlayLists())
        }
    }

    fun getPlayLists(): LiveData<List<PlayListEntity>> = livePlayLists
    fun onPlayListClick(playList: PlayListEntity) {
        Log.d("TAG", "Отправляем на список треков плейлиста")
    }
}