package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

interface FilesInterActor {
    suspend fun saveFile(uri: Uri): Uri
    suspend fun loadFile()
    suspend fun deletePlayListCover(uri: Uri)
}