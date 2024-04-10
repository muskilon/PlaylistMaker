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
    private fun getNewFile(): File {
        var biggest = 0
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playListCovers"
        )

//        filePath.deleteRecursively() //Удаление файлов в папке

        if (!filePath.exists()) filePath.mkdirs()
        if (!filePath.listFiles().isNullOrEmpty()) {
            filePath.listFiles()!!.forEach { fileName ->
                val sub =
                    fileName.toString()
                        .substringAfterLast("/cover")
                        .substringBeforeLast('.')
                        .toInt()
                if (sub > biggest) biggest = sub
            }
            return File(filePath, "$DEFAULT_FILE_NAME${biggest + 1}$DEFAULT_FILE_TYPE")
        } else return File(filePath, BEGIN_FILENAME)
    }
    override fun saveFile(uri: Uri): Uri {
        val file = getNewFile()
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        Log.d("DIR", file.toString())
        return file.toUri()
    }

    override fun loadFile() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DEFAULT_FILE_NAME = "cover"
        private const val DEFAULT_FILE_TYPE = ".jpg"
        private const val BEGIN_FILENAME = "cover1.jpg"
    }
}