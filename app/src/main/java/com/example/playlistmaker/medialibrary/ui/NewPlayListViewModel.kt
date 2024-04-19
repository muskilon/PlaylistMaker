package com.example.playlistmaker.medialibrary.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor
import com.example.playlistmaker.medialibrary.domain.TrackList
import kotlinx.coroutines.launch

open class NewPlayListViewModel(
    private val filesInteractor: FilesInteractor,
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    open fun createPlayList(title: String, description: String, uri: Uri?) {
        viewModelScope.launch {
            var newUri = uri
            uri?.let { newUri = filesInteractor.saveFile(it) }
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
        }
    }
}