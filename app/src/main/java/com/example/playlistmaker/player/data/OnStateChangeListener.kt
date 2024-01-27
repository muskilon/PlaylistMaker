package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.MusicPlayerState

interface OnStateChangeListener {
    fun onChange(state: MusicPlayerState)
}