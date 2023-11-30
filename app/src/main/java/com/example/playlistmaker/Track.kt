package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Track(
    val trackId: String, // Id трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl: String,
    val collectionName: String,
    val country: String,
    val primaryGenreName: String,
    val releaseDate: Date,
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