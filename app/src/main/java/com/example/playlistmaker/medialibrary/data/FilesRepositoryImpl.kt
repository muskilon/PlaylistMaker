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

class FilesRepositoryImpl(
    val context: Context
) : FilesRepository {
    override fun saveFile(uri: Uri): Uri {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playListCovers"
        ) //создаём экземпляр класса File, который указывает на нужный каталог
        if (!filePath.exists()) filePath.mkdirs() //создаем каталог, если он не создан
        val file = File(
            filePath,
            "first_cover.jpg"
        ) //создаём экземпляр класса File, который указывает на файл внутри каталога
        val inputStream =
            context.contentResolver.openInputStream(uri) // создаём входящий поток байтов из выбранной картинки
        val outputStream =
            FileOutputStream(file) // создаём исходящий поток байтов в созданный выше файл
        BitmapFactory // записываем картинку с помощью BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        Log.d("Repository", file.toUri().toString())
        return file.toUri()
    }

    override fun loadFile() {
        TODO("Not yet implemented")
    }
}