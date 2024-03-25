package com.example.playlistmaker.search.domain

data class Track(
    val trackId: String = "error", // Id трека
    val trackName: String = "error", // Название композиции
    val artistName: String = "error", // Имя исполнителя
    val trackTime: String = "error",
    val artworkUrl100: String = "error", // Ссылка на изображение обложки
    val artworkUrl512: String = "error",
    val previewUrl: String = "error",
    val collectionName: String = "error",
    val country: String = "error",
    val primaryGenreName: String = "error",
    val year: String = "error",
    var isFavorites: Boolean = false
) {
    override fun hashCode(): Int {
        return trackId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Track)
            return false
        return this.trackId == other.trackId
    }
}