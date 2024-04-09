package com.example.playlistmaker.medialibrary.domain

import android.net.Uri
import android.util.Log

class FilesInteractorImpl(
    private val repository: FilesRepository
) : FilesInteractor {
    override fun saveFile(uri: Uri): Uri {
        Log.d("Interactor", repository.saveFile(uri).toString())
        return repository.saveFile(uri)
    }

    override fun loadFile() {
        repository.loadFile()
    }
}