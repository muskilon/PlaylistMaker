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
    fun updatePlayList(title: String, description: String, uri: Uri?): Boolean {

        currentPlayList.value?.let {
            if (it.cover != uri || it.title != title || it.description != description) {
                viewModelScope.launch {
                    var newUri = uri
                    if (uri != null && uri != it.cover) {
                        newUri = filesInterActor.saveFile(uri)
                        it.cover?.let { oldCover -> filesInterActor.deletePlayListCover(oldCover) }
                    }
                    playListInterActor.updateSinglePlayList(
                        it.copy(
                            title = title,
                            description = description,
                            cover = newUri
                        )
                    )
                }
                return true
            } else return false
        } ?: return false
    }

    fun loadPlayList(playlistId: Long) {
        viewModelScope.launch {
            currentPlayList.postValue(playListInterActor.getSinglePlayList(playlistId))
        }
    }

    fun getPlayLists(): LiveData<PlayList> = currentPlayList
}