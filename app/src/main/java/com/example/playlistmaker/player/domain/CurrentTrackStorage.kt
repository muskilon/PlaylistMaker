package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

object CurrentTrackStorage {

    private var currentTrack: TrackModel = TrackModel()

    fun getTrackModel(): TrackModel {
        return currentTrack
    }

    fun setTrackModel(track: Track) {
        currentTrack.trackName = track.trackName
        currentTrack.artist = track.artistName
        currentTrack.album = track.collectionName
        currentTrack.duration = track.trackTime
        currentTrack.year = track.year
        currentTrack.country = track.country
        currentTrack.pictureUrl = track.artworkUrl512
        currentTrack.genre = track.primaryGenreName
        currentTrack.previewUrl = track.previewUrl
    }
}