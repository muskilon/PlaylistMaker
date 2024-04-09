package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

interface FilesRepository {
    fun saveFile(uri: Uri): Uri
    fun loadFile()

}