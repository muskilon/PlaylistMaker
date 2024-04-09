package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

interface FilesInteractor {
    fun saveFile(uri: Uri): Uri
    fun loadFile()
}