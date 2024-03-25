package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track

sealed class PlayerState {
    object Loading : PlayerState()
    data class Content(
        var trackModel: Track
    ) : PlayerState()
}