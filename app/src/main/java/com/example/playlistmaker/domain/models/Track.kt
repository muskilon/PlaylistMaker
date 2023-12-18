package com.example.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: String, // Id трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String,
    val artworkUrl100: String, // Ссылка на изображение обложки
    val artworkUrl512: String,
    val previewUrl: String,
    val collectionName: String,
    val country: String,
    val primaryGenreName: String,
    val year: String,
) : Parcelable