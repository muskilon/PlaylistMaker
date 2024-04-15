package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

interface FilesInteractor {
    suspend fun saveFile(uri: Uri): Uri
    suspend fun loadFile()
    suspend fun deletePlayList(uri: Uri)
}