package com.example.playlistmaker.medialibrary.domain

import android.net.Uri

interface FilesRepository {
    suspend fun saveFile(uri: Uri): Uri
    suspend fun loadFile()
    suspend fun deleteFile(uri: Uri)

}