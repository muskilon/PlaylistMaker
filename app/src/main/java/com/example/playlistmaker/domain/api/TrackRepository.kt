package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchSongs(entity: String, term: String, lang: String): List<Track>
}