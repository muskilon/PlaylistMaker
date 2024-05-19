package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

interface CurrentTrackInterActor {
    fun getCurrentTrack(): Track
}