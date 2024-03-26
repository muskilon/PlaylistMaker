package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

data class PlayStatus(
    var timeElapsed: String,
    var playButtonClickableState: Boolean,
    var playButtonImage: String,
    var currentTrack: Track,
    var isFavorites: Boolean
)
