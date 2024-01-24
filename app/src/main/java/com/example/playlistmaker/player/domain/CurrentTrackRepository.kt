package com.example.playlistmaker.player.domain

interface CurrentTrackRepository {
    fun getCurrentTrack(): TrackModel
}