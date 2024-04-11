package com.example.playlistmaker.medialibrary.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.medialibrary.domain.FilesRepository
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class FilesRepositoryImpl(
    val context: Context
) : FilesRepository {
    override fun saveFile(uri: Uri): Uri {
        //        filePath.deleteRecursively() //Удаление файлов в папке
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playListCovers"
        )
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, "${UUID.randomUUID()}$DEFAULT_FILE_TYPE")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        filePath.listFiles()?.forEach {
            Log.d("DIR", it.name)
        }
        return file.toUri()
    }

    override fun loadFile() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DEFAULT_FILE_TYPE = ".jpg"
    }
}