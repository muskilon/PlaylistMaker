package com.example.playlistmaker.domain.models

enum class PlayerState(val value: Int) {
    STATE_END_OF_SONG(0),
    STATE_PREPARED(1)
}