package com.example.playlistmaker.medialibrary.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import kotlinx.coroutines.launch

class EditPlayListViewModel(
    private val filesInteractor: FilesInteractor,
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {
    private var currentPlayList = MutableLiveData<PlayList>()
    fun updatePlayList(title: String, description: String, uri: Uri?) {
        viewModelScope.launch {
            var newUri = uri
            if (uri != null && uri != currentPlayList.value?.cover) {
                newUri = filesInteractor.saveFile(uri)
                currentPlayList.value?.let { playList ->
                    playList.cover?.let { filesInteractor.deletePlayListCover(it) }
                }
            }
            playListInteractor.updateSinglePlayList(
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
            currentPlayList.postValue(playListInteractor.getSinglePlayList(playlistId))
        }
    }

    fun getPlayLists(): LiveData<PlayList> = currentPlayList
}