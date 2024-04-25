package com.example.playlistmaker.medialibrary.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.FilesInterActor
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInterActor
import kotlinx.coroutines.launch

class EditPlayListViewModel(
    private val filesInterActor: FilesInterActor,
    private val playListInterActor: PlayListInterActor,
) : ViewModel() {
    private var currentPlayList = MutableLiveData<PlayList>()
    fun updatePlayList(title: String, description: String, uri: Uri?) {
        viewModelScope.launch {
            var newUri = uri
            if (uri != null && uri != currentPlayList.value?.cover) {
                newUri = filesInterActor.saveFile(uri)
                currentPlayList.value?.let { playList ->
                    playList.cover?.let { filesInterActor.deletePlayListCover(it) }
                }
            }
            playListInterActor.updateSinglePlayList(
                PlayList(
                    title = title,
                    description = description,
                    cover = newUri,
                    id = currentPlayList.value!!.id,
                    trackCount = currentPlayList.value!!.trackCount,
                    tracks = currentPlayList.value!!.tracks
                )
            )
        }
    }

    fun loadPlayList(playlistId: Long) {
        viewModelScope.launch {
            currentPlayList.postValue(playListInterActor.getSinglePlayList(playlistId))
        }
    }

    fun getPlayLists(): LiveData<PlayList> = currentPlayList
}