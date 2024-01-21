package com.example.playlistmaker.search.domain

interface TrackRepository {
    fun searchSongs(entity: String, term: String, lang: String): Resource<List<Track>>
}