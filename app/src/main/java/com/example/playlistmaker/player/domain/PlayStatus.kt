package com.example.playlistmaker.player.domain

data class PlayStatus(
    var timeElapsed: String,
    var playButtonClickableState: Boolean,
    var playButtonImage: String,
    var currentTrack: TrackModel
)
