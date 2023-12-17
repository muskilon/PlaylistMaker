package com.example.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Parcelize
data class Track(
    val trackId: String = "error", // Id трека
    val trackName: String = "error", // Название композиции
    val artistName: String = "error", // Имя исполнителя
    val trackTimeMillis: Long = 0, // Продолжительность трека
    val artworkUrl100: String = "error", // Ссылка на изображение обложки
    val previewUrl: String = "error",
    val collectionName: String = "error",
    val country: String = "error",
    val primaryGenreName: String = "error",
    val releaseDate: Date = Calendar.getInstance().time

) : Parcelable {
    val trackTime: String
        get() {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.trackTimeMillis)
        }
    val year: String
        get() {
            return SimpleDateFormat("yyyy", Locale.getDefault()).format(this.releaseDate)
        }
    val artworkUrl512: String
        get() {
            return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        }
}