package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

class FilesInteractorImpl(
    private val repository: FilesRepository
) : FilesInteractor {
    override fun saveFile(uri: Uri): Uri {
        return repository.saveFile(uri)
    }

    override fun loadFile() {
        repository.loadFile()
    }
}