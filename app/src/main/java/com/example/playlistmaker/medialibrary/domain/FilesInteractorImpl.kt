package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

class FilesInteractorImpl(
    private val repository: FilesRepository
) : FilesInteractor {

    override suspend fun saveFile(uri: Uri): Uri {
        return repository.saveFile(uri)
    }

    override suspend fun loadFile() {
        repository.loadFile()
    }

    override suspend fun deletePlayListCover(uri: Uri) {
        repository.deleteFile(uri)
    }
}