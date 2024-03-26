package com.example.playlistmaker.player.data

import com.example.playlistmaker.search.domain.Track

object CurrentTrackStorage {

    private var currentTrack: Track = Track()

    fun getTrackModel(): Track {
        return currentTrack
    }

    fun setCurrentTrack(track: Track) {
        currentTrack = track
    }
}