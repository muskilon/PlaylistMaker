package com.example.playlistmaker.medialibrary.ui

import android.net.Uri
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
    fun saveFile(uri: Uri): Uri {
        return filesInteractor.saveFile(uri)
    }

    fun createPlayList(title: String, description: String, uri: String) {
        viewModelScope.launch {
            playListInteractor.addPlayList(
                PlayList(
                    title = title,
                    description = description,
                    cover = uri,
                    id = 0L,
                    trackCount = 0,
                    tracks = TrackList(mutableListOf())
                )
            )
        }
    }
}