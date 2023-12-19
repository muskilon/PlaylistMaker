package com.example.playlistmaker.domain

interface TrackRepository {
    fun searchSongs(entity: String, term: String, lang: String): Resource<List<Track>>
}