package com.example.playlistmaker.domain

sealed class PlayerState {
    object Loading : PlayerState()
    data class Content(
        val trackModel: TrackModel,
        var timeElapsed: String,
        var playButtonClickableState: Boolean,
        var playButtonImage: Int
    ) : PlayerState()
}