package com.example.playlistmaker.medialibrary.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.playlistmaker.medialibrary.domain.FilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class FilesRepositoryImpl(
    val context: Context
) : FilesRepository {
    override suspend fun saveFile(uri: Uri): Uri {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playListCovers"
        )
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, "${UUID.randomUUID()}$DEFAULT_FILE_TYPE")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }

    override suspend fun loadFile() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFile(uri: Uri) {
        uri.toFile().delete()
    }

    companion object {
        private const val DEFAULT_FILE_TYPE = ".jpg"
    }
}