package com.example.playlistmaker.medialibrary.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import com.example.playlistmaker.medialibrary.domain.TrackList
import kotlinx.coroutines.launch

class NewPlayListViewModel(
    private val filesInteractor: FilesInteractor,
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    fun createPlayList(title: String, description: String, uri: Uri?) {
        viewModelScope.launch {
            var newUri = uri
            if (uri != null) {
                newUri = filesInteractor.saveFile(uri)
            }
            playListInteractor.addPlayList(
                PlayList(
                    title = title,
                    description = description,
                    cover = newUri,
                    id = 0L,
                    trackCount = 0,
                    tracks = TrackList(mutableListOf())
                )
            )
            Log.d("TAG", newUri.toString())
        }
    }
}