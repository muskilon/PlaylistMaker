package com.example.playlistmaker.player.domain

sealed class PlayerState {
    object Loading : PlayerState()
    data class Content(
        var trackModel: TrackModel
    ) : PlayerState()
}