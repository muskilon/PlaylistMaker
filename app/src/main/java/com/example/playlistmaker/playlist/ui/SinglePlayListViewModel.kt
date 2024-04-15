package com.example.playlistmaker.playlist.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.medialibrary.domain.FilesInteractor
import com.example.playlistmaker.medialibrary.domain.PlayListInteractor

class SinglePlayListViewModel(
    private val filesInteractor: FilesInteractor,
    private val playListInteractor: PlayListInteractor
) : ViewModel()